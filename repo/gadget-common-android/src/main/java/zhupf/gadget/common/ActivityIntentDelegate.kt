package zhupf.gadget.common

import android.app.Activity
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KProperty

abstract class IntentValue<A : Activity, V>(val key: String?) {
    abstract operator fun getValue(activity: A, property: KProperty<*>): V?
    abstract operator fun setValue(activity: A, property: KProperty<*>, value: V?)
}

class IntentByte<A : Activity>(val defaultValue: Byte, key: String?) : IntentValue<A, Byte>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Byte {
        return activity.intent.getByteExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Byte?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentByte(defaultValue: Byte = 0, key: String? = null) = IntentByte<A>(defaultValue, key)


class IntentShort<A : Activity>(val defaultValue: Short, key: String?) : IntentValue<A, Short>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Short {
        return activity.intent.getShortExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Short?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentShort(defaultValue: Short = 0, key: String? = null) = IntentShort<A>(defaultValue, key)


class IntentInt<A : Activity>(val defaultValue: Int, key: String?) : IntentValue<A, Int>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Int {
        return activity.intent.getIntExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Int?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentInt(defaultValue: Int = 0, key: String? = null) = IntentInt<A>(defaultValue, key)


class IntentLong<A : Activity>(val defaultValue: Long, key: String?) : IntentValue<A, Long>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Long {
        return activity.intent.getLongExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Long?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentLong(defaultValue: Long = 0L, key: String? = null) = IntentLong<A>(defaultValue, key)


class IntentFloat<A : Activity>(val defaultValue: Float, key: String?) : IntentValue<A, Float>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Float {
        return activity.intent.getFloatExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Float?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentFloat(defaultValue: Float = 0F, key: String? = null) = IntentFloat<A>(defaultValue, key)


class IntentDouble<A : Activity>(val defaultValue: Double, key: String?) : IntentValue<A, Double>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Double {
        return activity.intent.getDoubleExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Double?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentDouble(defaultValue: Double = 0.0, key: String? = null) = IntentDouble<A>(defaultValue, key)


class IntentBoolean<A : Activity>(val defaultValue: Boolean, key: String?) : IntentValue<A, Boolean>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Boolean {
        return activity.intent.getBooleanExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Boolean?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentBoolean(defaultValue: Boolean = false, key: String? = null) = IntentBoolean<A>(defaultValue, key)


class IntentChar<A : Activity>(val defaultValue: Char, key: String?) : IntentValue<A, Char>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Char {
        return activity.intent.getCharExtra(key ?: property.name, defaultValue)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Char?) {
        assert(value != null)
        activity.intent.putExtra(key ?: property.name, value ?: defaultValue)
    }
}
fun <A : Activity> A.intentChar(defaultValue: Char = ' ', key: String? = null) = IntentChar<A>(defaultValue, key)


class IntentString<A : Activity>(key: String?) : IntentValue<A, String>(key) {
    override fun getValue(activity: A, property: KProperty<*>): String? {
        return activity.intent.getStringExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: String?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentString(key: String? = null) = IntentString<A>(key)


class IntentCharSequence<A : Activity, V : CharSequence>(key: String?) : IntentValue<A, V>(key) {
    override fun getValue(activity: A, property: KProperty<*>): V? {
        return activity.intent.getCharSequenceExtra(key ?: property.name) as? V
    }
    override fun setValue(activity: A, property: KProperty<*>, value: V?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity, V : CharSequence> A.intentCharSequence(key: String? = null) = IntentCharSequence<A, V>(key)


class IntentSerializable<A : Activity, V : Serializable>(key: String?) : IntentValue<A, V>(key) {
    override fun getValue(activity: A, property: KProperty<*>): V? {
        return activity.intent.getSerializableExtra(key ?: property.name) as? V
    }
    override fun setValue(activity: A, property: KProperty<*>, value: V?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity, V : Serializable> A.intentSerializable(key: String? = null) = IntentSerializable<A, V>(key)


class IntentParcelable<A : Activity, V : Parcelable>(key: String?) : IntentValue<A, V>(key) {
    override fun getValue(activity: A, property: KProperty<*>): V? {
        return activity.intent.getParcelableExtra(key ?: property.name) as? V
    }
    override fun setValue(activity: A, property: KProperty<*>, value: V?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity, V : Parcelable> A.intentParcelable(key: String? = null) = IntentParcelable<A, V>(key)


class IntentByteArray<A : Activity>(key: String?) : IntentValue<A, ByteArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): ByteArray? {
        return activity.intent.getByteArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: ByteArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentByteArray(key: String? = null) = IntentByteArray<A>(key)


class IntentShortArray<A : Activity>(key: String?) : IntentValue<A, ShortArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): ShortArray? {
        return activity.intent.getShortArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: ShortArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentShortArray(key: String? = null) = IntentShortArray<A>(key)


class IntentIntArray<A : Activity>(key: String?) : IntentValue<A, IntArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): IntArray? {
        return activity.intent.getIntArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: IntArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentIntArray(key: String? = null) = IntentIntArray<A>(key)


class IntentLongArray<A : Activity>(key: String?) : IntentValue<A, LongArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): LongArray? {
        return activity.intent.getLongArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: LongArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentLongArray(key: String? = null) = IntentLongArray<A>(key)


class IntentFloatArray<A : Activity>(key: String?) : IntentValue<A, FloatArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): FloatArray? {
        return activity.intent.getFloatArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: FloatArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentFloatArray(key: String? = null) = IntentFloatArray<A>(key)


class IntentDoubleArray<A : Activity>(key: String?) : IntentValue<A, DoubleArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): DoubleArray? {
        return activity.intent.getDoubleArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: DoubleArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentDoubleArray(key: String? = null) = IntentDoubleArray<A>(key)


class IntentBooleanArray<A : Activity>(key: String?) : IntentValue<A, BooleanArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): BooleanArray? {
        return activity.intent.getBooleanArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: BooleanArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentBooleanArray(key: String? = null) = IntentBooleanArray<A>(key)


class IntentCharArray<A : Activity>(key: String?) : IntentValue<A, CharArray>(key) {
    override fun getValue(activity: A, property: KProperty<*>): CharArray? {
        return activity.intent.getCharArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: CharArray?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentCharArray(key: String? = null) = IntentCharArray<A>(key)


class IntentStringArray<A : Activity>(key: String?) : IntentValue<A, Array<String>>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Array<String>? {
        return activity.intent.getStringArrayExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Array<String>?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentStringArray(key: String? = null) = IntentStringArray<A>(key)


class IntentCharSequenceArray<A : Activity, V : CharSequence>(key: String?) : IntentValue<A, Array<V>>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Array<V>? {
        return activity.intent.getCharSequenceArrayExtra(key ?: property.name) as? Array<V>
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Array<V>?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity, V : CharSequence> A.intentCharSequenceArray(key: String? = null) = IntentCharSequenceArray<A, V>(key)


class IntentParcelableArray<A : Activity, V : Parcelable>(key: String?) : IntentValue<A, Array<V>>(key) {
    override fun getValue(activity: A, property: KProperty<*>): Array<V>? {
        return activity.intent.getParcelableArrayExtra(key ?: property.name) as? Array<V>
    }
    override fun setValue(activity: A, property: KProperty<*>, value: Array<V>?) {
        activity.intent.putExtra(key ?: property.name, value)
    }
}
fun <A : Activity, V : Parcelable> A.intentParcelableArray(key: String? = null) = IntentParcelableArray<A, V>(key)


class IntentIntList<A : Activity>(key: String?) : IntentValue<A, ArrayList<Int>>(key) {
    override fun getValue(activity: A, property: KProperty<*>): ArrayList<Int>? {
        return activity.intent.getIntegerArrayListExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: ArrayList<Int>?) {
        activity.intent.putIntegerArrayListExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentIntList(key: String? = null) = IntentIntList<A>(key)


class IntentStringList<A : Activity>(key: String?) : IntentValue<A, ArrayList<String>>(key) {
    override fun getValue(activity: A, property: KProperty<*>): ArrayList<String>? {
        return activity.intent.getStringArrayListExtra(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: ArrayList<String>?) {
        activity.intent.putStringArrayListExtra(key ?: property.name, value)
    }
}
fun <A : Activity> A.intentStringList(key: String? = null) = IntentStringList<A>(key)


class IntentCharSequenceList<A : Activity, V : CharSequence>(key: String?) : IntentValue<A, ArrayList<V>>(key) {
    override fun getValue(activity: A, property: KProperty<*>): ArrayList<V>? {
        return activity.intent.getCharSequenceArrayListExtra(key ?: property.name) as? ArrayList<V>
    }
    override fun setValue(activity: A, property: KProperty<*>, value: ArrayList<V>?) {
        activity.intent.putCharSequenceArrayListExtra(
            key ?: property.name,
            value as ArrayList<CharSequence>
        )
    }
}
fun <A : Activity, V : CharSequence> A.intentCharSequenceList(key: String? = null) = IntentCharSequenceList<A, V>(key)


class IntentParcelableList<A : Activity, V : Parcelable>(key: String?) : IntentValue<A, ArrayList<V>>(key) {
    override fun getValue(activity: A, property: KProperty<*>): ArrayList<V>? {
        return activity.intent.getParcelableArrayListExtra<V>(key ?: property.name)
    }
    override fun setValue(activity: A, property: KProperty<*>, value: ArrayList<V>?) {
        activity.intent.putParcelableArrayListExtra(key ?: property.name, value)
    }
}
fun <A : Activity, V : Parcelable> A.intentParcelableList(key: String? = null) = IntentParcelableList<A, V>(key)