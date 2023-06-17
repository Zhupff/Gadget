package zhupf.gadget.of

@Suppress("UNCHECKED_CAST")
class OfLoader<T>(val loader: ClassLoader, val of: Class<T>, vararg val constructorParameterTypes: Class<*>) {

    constructor(of: Class<T>, vararg constructorParameterTypes: Class<*>):
        this(DEFAULT_CLASS_LOADER ?: Thread.currentThread().contextClassLoader, of, *constructorParameterTypes)

    companion object {
        private val CLASS_OF = ClassOf::class.java.simpleName
        private val OBJECT_OF = ObjectOf::class.java.simpleName

        var DEFAULT_CLASS_LOADER: ClassLoader? = null
    }

    val classOf: ArrayList<Class<T>> = ArrayList(8)

    val objectOf: ArrayList<Class<T>> = ArrayList(2)

    val size: Int; get() = classOf.size + objectOf.size

    fun load() = apply {
        if (size <= 0) {
            loader.getResources("META-INF/services/${of.canonicalName}_OF")
                .toList()
                .flatMap { it.openStream().reader(Charsets.UTF_8).readLines() }
                .forEach { content ->
                    if (content.contains('-')) {
                        val (type, name) = content.split('-')
                        when (type) {
                            CLASS_OF -> classOf.add(Class.forName(name) as Class<T>)
                            OBJECT_OF -> objectOf.add(Class.forName(name) as Class<T>)
                        }
                    }
                }
        }
    }

    fun create(vararg parameters: Any?): List<T> = createClassOnly(*parameters) + createObjectOnly()

    fun createClassOnly(vararg parameters: Any?): List<T> {
        val instances = ArrayList<T>(classOf.size)
        classOf.forEach { cls ->
            val constructor = cls.getConstructor(*constructorParameterTypes)
            constructor.isAccessible = true
            instances.add(constructor.newInstance(*parameters))
        }
        return instances
    }

    fun createObjectOnly(): List<T> {
        val instances = ArrayList<T>(objectOf.size)
        objectOf.forEach { cls ->
            instances.add(cls.getDeclaredField(ObjectOf.FIELD_NAME).get(null) as T)
        }
        return instances
    }
}