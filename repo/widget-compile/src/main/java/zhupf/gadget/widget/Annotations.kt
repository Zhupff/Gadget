package zhupf.gadget.widget

@DslMarker
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.SOURCE)
annotation class DslScope


@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class WidgetX(val name: String = "", val enableDsl: Boolean = false)


@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class WidgetDsl(val name: String = "")


@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class LayoutParamsDsl(val name: String = "")