package zhupf.gadgets

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class GadgetName(val value: String)