package gadget.component.role

import com.google.auto.service.AutoService
import gadget.basic.Gadget
import gadget.basic.exception.throws
import gadget.basic.tool.saveTo
import gadget.basic.tool.unzip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

@AutoService(IComponentRole::class)
internal class ComponentRole : IComponentRole {

    companion object {
        private val assetsDir: File = Gadget.application.filesDir
            .resolve("_ASSETS_/_ROLE_").also(File::mkdirs)
        private val latestResource: String? by lazy {
            Gadget.application.assets.list("role")
                ?.filter { it.startsWith("role-") && it.endsWith(".zip") }
                ?.maxOrNull()
        }

        private val loading = AtomicBoolean(false)

        fun checkResourcesExist(): Boolean {
            val t1 = System.currentTimeMillis()
            if (latestResource != null) {
                val targetAsset = latestResource!!.removeSuffix(".zip")
                val targetDir = assetsDir.resolve(targetAsset)
                if (targetDir.exists()) {
                    println("@@@ ${System.currentTimeMillis() - t1}")
                    return true
                }
            }
            println("@@@ ${System.currentTimeMillis() - t1}")
            return false
        }

        fun loadResources() {
            if (loading.compareAndSet(false, true)) {
                GlobalScope.launch(Dispatchers.IO) {
                    var zipFile: File? = null
                    try {
                        val asset = Gadget.application.assets.list("role")
                            ?.filter { it.startsWith("role-") && it.endsWith(".zip") }
                            ?.maxOrNull()
                        if (asset != null) {
                            val targetAsset = asset.removeSuffix(".zip")
                            val targetDir = assetsDir.resolve(targetAsset)
                            if (!targetDir.exists()) {
                                zipFile = assetsDir.resolve(asset)
                                Gadget.application.assets.open("role/$asset").saveTo(zipFile)
                                zipFile.unzip(targetDir) {
                                    println("@@@ unzip: ${it}")
                                }
                            } else {
                                println("@@@ loaded")
                            }
                        }
                    } catch (throwable: Throwable) {
                        if (Gadget.debuggable) {
                            throwable.throws()
                        }
                    } finally {
                        zipFile?.deleteRecursively()
                        loading.set(false)
                    }
                }
            }
        }
    }
}