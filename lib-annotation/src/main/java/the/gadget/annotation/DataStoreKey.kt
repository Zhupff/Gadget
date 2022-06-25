package the.gadget.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class DataStoreKey(val value: String)