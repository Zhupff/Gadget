package the.gadget.api

import the.gadget.GadgetApplication
import the.gadget.module.base.BuildConfig
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
object Api {

    private val globalApiIndices: Map<KClass<*>, Class<*>> = GadgetApplication.instance.classLoader
        .let { classLoader ->
            val indices = HashMap<KClass<*>, Class<*>>()
            val releaseApis = classLoader.getResources("META-INF/services/${GlobalApi::class.java.canonicalName}").toList()
            val debugApis = classLoader.getResources("META-INF/services/${GlobalDebugApi::class.java.canonicalName}").toList()
            (releaseApis + debugApis).flatMap { api ->
                val className: List<String>
                api.openStream().reader(Charsets.UTF_8).use { reader ->
                    className = reader.readLines()
                }
                className
            }.let { classNames ->
                for (className in classNames) {
                    val implClass = Class.forName(className)
                    val apiClass = implClass.getAnnotation(GlobalApi::class.java)?.api
                        ?: implClass.getAnnotation(GlobalDebugApi::class.java)?.api
                        ?: continue
                        indices.putIfAbsent(apiClass, implClass)
                }
            }
            indices
        }

    private val scopeApiIndices: Map<KClass<*>, Class<*>> = GadgetApplication.instance.classLoader
        .let { classLoader ->
            val indices = HashMap<KClass<*>, Class<*>>()
            val releaseApis = classLoader.getResources("META-INF/services/${ScopeApi::class.java.canonicalName}").toList()
            val debugApis = classLoader.getResources("META-INF/services/${ScopeDebugApi::class.java.canonicalName}").toList()
            (releaseApis + debugApis).flatMap { api ->
                val className: List<String>
                api.openStream().reader(Charsets.UTF_8).use { reader ->
                    className = reader.readLines()
                }
                className
            }.let { classNames ->
                for (className in classNames) {
                    val implClass = Class.forName(className)
                    val apiClass = implClass.getAnnotation(ScopeApi::class.java)?.api
                        ?: implClass.getAnnotation(ScopeDebugApi::class.java)?.api
                        ?: continue
                    indices.putIfAbsent(apiClass, implClass)
                }
            }
            indices
        }

    init {
        if (BuildConfig.DEBUG) {
            checkCircularDependencies()
        }
    }

    private val globalApiInstances: HashMap<KClass<*>, Any> = HashMap()

    fun init() {
        globalApiIndices.forEach { (apiClass, implClass) ->
            val lazy = implClass.getAnnotation(GlobalApi::class.java)?.lazy
                ?: implClass.getAnnotation(GlobalDebugApi::class.java)?.lazy
                ?: true
            if (!lazy) {
                newGlobalApi(apiClass)
            }
        }
    }

    private fun checkCircularDependencies() {
        val list =  globalApiIndices.map { (apiClass, implClass) ->
            val dependencies = implClass.getAnnotation(GlobalApi::class.java)?.dependencies
                ?: implClass.getAnnotation(GlobalDebugApi::class.java)?.dependencies
                ?: emptyArray()
            apiClass to dependencies.toMutableList()
        }.toMutableList()
        while (list.isNotEmpty()) {
            val item = list.find { it.second.size == 0 }
                ?: throw IllegalStateException("There is circular dependency in ${list.map { it.first }}.")
            list.remove(item)
            list.forEach { it.second.remove(item.first) }
        }
    }

    private fun newGlobalApi(apiClass: KClass<*>) {
        val implClass = globalApiIndices[apiClass] ?: return
        val dependencies = implClass.getAnnotation(GlobalApi::class.java)?.dependencies
            ?: implClass.getAnnotation(GlobalDebugApi::class.java)?.dependencies
            ?: emptyArray()
        dependencies.forEach { dependency ->
            if (!globalApiInstances.containsKey(dependency)) {
                newGlobalApi(dependency)
            }
        }
        if (!globalApiInstances.containsKey(apiClass)) {
            globalApiInstances[apiClass] = implClass.newInstance()
        }
    }

    internal fun <T : Any> getGlobalApi(apiClass: KClass<T>): T? {
        var impl = globalApiInstances[apiClass]
        if (impl == null) {
            synchronized(globalApiInstances) {
                impl = globalApiInstances[apiClass]
                if (impl == null) {
                    newGlobalApi(apiClass)
                    impl = globalApiInstances[apiClass]
                }
            }
        }
        return impl as? T
    }

    internal fun <T : Any> newScopeApi(apiClass: KClass<T>): T? = scopeApiIndices[apiClass]?.newInstance() as? T
}

fun <T : Any> KClass<T>.globalApi(): T = Api.getGlobalApi(this)!!

fun <T : NullableApi> KClass<T>.globalApi(): T? = Api.getGlobalApi(this)

fun <T : Any> KClass<T>.scopeApi(): T = Api.newScopeApi(this)!!

fun <T : NullableApi> KClass<T>.scopeApi(): T? = Api.newScopeApi(this)