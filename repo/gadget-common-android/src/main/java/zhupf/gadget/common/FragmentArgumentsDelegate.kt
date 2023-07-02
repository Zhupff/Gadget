package zhupf.gadget.common

import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.reflect.KProperty

abstract class ArgumentValue<F : Fragment, V>(val key: String?) {
    abstract operator fun getValue(fragment: F, property: KProperty<*>): V?
    abstract operator fun setValue(fragment: F, property: KProperty<*>, value: V?)
}

class ArgumentByte<F : Fragment>(val defaultValue: Byte, key: String?) : ArgumentValue<F, Byte>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Byte {
        return fragment.arguments?.getByte(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Byte?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putByte(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentByte(defaultValue: Byte = 0, key: String?) = ArgumentByte<F>(defaultValue, key)


class ArgumentShort<F : Fragment>(val defaultValue: Short, key: String?) : ArgumentValue<F, Short>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Short {
        return fragment.arguments?.getShort(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Short?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putShort(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentShort(defaultValue: Short = 0, key: String? = null) = ArgumentShort<F>(defaultValue, key)


class ArgumentInt<F : Fragment>(val defaultValue: Int, key: String?) : ArgumentValue<F, Int>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Int {
        return fragment.arguments?.getInt(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Int?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putInt(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentInt(defaultValue: Int = 0, key: String? = null) = ArgumentInt<F>(defaultValue, key)


class ArgumentLong<F : Fragment>(val defaultValue: Long, key: String?) : ArgumentValue<F, Long>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Long {
        return fragment.arguments?.getLong(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Long?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putLong(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentLong(defaultValue: Long = 0L, key: String? = null) = ArgumentLong<F>(defaultValue, key)


class ArgumentFloat<F : Fragment>(val defaultValue: Float, key: String?) : ArgumentValue<F, Float>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Float {
        return fragment.arguments?.getFloat(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Float?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putFloat(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentFloat(defaultValue: Float = 0F, key: String? = null) = ArgumentFloat<F>(defaultValue, key)


class ArgumentDouble<F : Fragment>(val defaultValue: Double, key: String?) : ArgumentValue<F, Double>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Double {
        return fragment.arguments?.getDouble(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Double?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putDouble(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentDouble(defaultValue: Double = 0.0, key: String? = null) = ArgumentDouble<F>(defaultValue, key)


class ArgumentBoolean<F : Fragment>(val defaultValue: Boolean, key: String?) : ArgumentValue<F, Boolean>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Boolean {
        return fragment.arguments?.getBoolean(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Boolean?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putBoolean(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentBoolean(defaultValue: Boolean = false, key: String? = null) = ArgumentBoolean<F>(defaultValue, key)


class ArgumentChar<F : Fragment>(val defaultValue: Char, key: String?) : ArgumentValue<F, Char>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Char {
        return fragment.arguments?.getChar(key ?: property.name, defaultValue) ?: defaultValue
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Char?) {
        assert(value != null)
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putChar(key ?: property.name, value ?: defaultValue)
    }
}
fun <F : Fragment> F.argumentChar(defaultValue: Char = ' ', key: String? = null) = ArgumentChar<F>(defaultValue, key)


class ArgumentString<F : Fragment>(key: String?) : ArgumentValue<F, String>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): String? {
        return fragment.arguments?.getString(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: String?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putString(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentString(key: String? = null) = ArgumentString<F>(key)


class ArgumentCharSequence<F : Fragment, V : CharSequence>(key: String?) : ArgumentValue<F, V>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): V? {
        return fragment.arguments?.getCharSequence(key ?: property.name) as? V
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: V?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putCharSequence(key ?: property.name, value)
    }
}
fun <F : Fragment, V : CharSequence> F.argumentCharSequence(key: String? = null) = ArgumentCharSequence<F, V>(key)


class ArgumentSerializable<F : Fragment, V : Serializable>(key: String?) : ArgumentValue<F, V>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): V? {
        return fragment.arguments?.getSerializable(key ?: property.name) as? V
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: V?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putSerializable(key ?: property.name, value)
    }
}
fun <F : Fragment, V : Serializable> F.argumentSerializable(key: String? = null) = ArgumentSerializable<F, V>(key)


class ArgumentParcelable<F : Fragment, V : Parcelable>(key: String?) : ArgumentValue<F, V>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): V? {
        return fragment.arguments?.getParcelable(key ?: property.name) as? V
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: V?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putParcelable(key ?: property.name, value)
    }
}
fun <F : Fragment, V : Parcelable> F.argumentParcelable(key: String? = null) = ArgumentParcelable<F, V>(key)


class ArgumentByteArray<F : Fragment>(key: String?) : ArgumentValue<F, ByteArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): ByteArray? {
        return fragment.arguments?.getByteArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: ByteArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putByteArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentByteArray(key: String? = null) = ArgumentByteArray<F>(key)


class ArgumentShortArray<F : Fragment>(key: String?) : ArgumentValue<F, ShortArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): ShortArray? {
        return fragment.arguments?.getShortArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: ShortArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putShortArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentShortArray(key: String? = null) = ArgumentShortArray<F>(key)


class ArgumentIntArray<F : Fragment>(key: String?) : ArgumentValue<F, IntArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): IntArray? {
        return fragment.arguments?.getIntArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: IntArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putIntArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentIntArray(key: String? = null) = ArgumentIntArray<F>(key)


class ArgumentLongArray<F : Fragment>(key: String?) : ArgumentValue<F, LongArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): LongArray? {
        return fragment.arguments?.getLongArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: LongArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putLongArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentLongArray(key: String? = null) = ArgumentLongArray<F>(key)


class ArgumentFloatArray<F : Fragment>(key: String?) : ArgumentValue<F, FloatArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): FloatArray? {
        return fragment.arguments?.getFloatArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: FloatArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putFloatArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentFloatArray(key: String? = null) = ArgumentFloatArray<F>(key)


class ArgumentDoubleArray<F : Fragment>(key: String?) : ArgumentValue<F, DoubleArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): DoubleArray? {
        return fragment.arguments?.getDoubleArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: DoubleArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putDoubleArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentDoubleArray(key: String? = null) = ArgumentDoubleArray<F>(key)


class ArgumentBooleanArray<F : Fragment>(key: String?) : ArgumentValue<F, BooleanArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): BooleanArray? {
        return fragment.arguments?.getBooleanArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: BooleanArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putBooleanArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentBooleanArray(key: String? = null) = ArgumentBooleanArray<F>(key)


class ArgumentCharArray<F : Fragment>(key: String?) : ArgumentValue<F, CharArray>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): CharArray? {
        return fragment.arguments?.getCharArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: CharArray?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putCharArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentCharArray(key: String? = null) = ArgumentCharArray<F>(key)


class ArgumentStringArray<F : Fragment>(key: String?) : ArgumentValue<F, Array<String>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Array<String>? {
        return fragment.arguments?.getStringArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Array<String>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putStringArray(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentStringArray(key: String? = null) = ArgumentStringArray<F>(key)


class ArgumentCharSequenceArray<F : Fragment, V : CharSequence>(key: String?) : ArgumentValue<F, Array<V>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Array<V>? {
        return fragment.arguments?.getCharSequenceArray(key ?: property.name) as? Array<V>
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Array<V>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putCharSequenceArray(key ?: property.name, value)
    }
}
fun <F : Fragment, V : CharSequence> F.argumentCharSequenceArray(key: String? = null) = ArgumentCharSequenceArray<F, V>(key)


class ArgumentParcelableArray<F : Fragment, V : Parcelable>(key: String?) : ArgumentValue<F, Array<V>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Array<V>? {
        return fragment.arguments?.getParcelableArray(key ?: property.name) as? Array<V>
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Array<V>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putParcelableArray(key ?: property.name, value)
    }
}
fun <F : Fragment, V : Parcelable> F.argumentParcelableArray(key: String? = null) = ArgumentParcelableArray<F, V>(key)


class ArgumentSparseParcelableArray<F : Fragment, V : Parcelable>(key: String?) : ArgumentValue<F, SparseArray<V>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): SparseArray<V>? {
        return fragment.arguments?.getSparseParcelableArray(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: SparseArray<V>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putSparseParcelableArray(key ?: property.name, value)
    }
}
fun <F : Fragment, V : Parcelable> F.argumentSparseParcelableArray(key: String? = null) = ArgumentSparseParcelableArray<F, V>(key)


class ArgumentIntList<F : Fragment>(key: String?) : ArgumentValue<F, ArrayList<Int>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): ArrayList<Int>? {
        return fragment.arguments?.getIntegerArrayList(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: ArrayList<Int>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putIntegerArrayList(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentIntList(key: String? = null) = ArgumentIntList<F>(key)


class ArgumentStringList<F : Fragment>(key: String?) : ArgumentValue<F, ArrayList<String>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): ArrayList<String>? {
        return fragment.arguments?.getStringArrayList(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: ArrayList<String>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putStringArrayList(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentStringList(key: String? = null) = ArgumentStringList<F>(key)


class ArgumentCharSequenceList<F : Fragment, V : CharSequence>(key: String?) : ArgumentValue<F, ArrayList<V>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): ArrayList<V>? {
        return fragment.arguments?.getCharSequenceArrayList(key ?: property.name) as? ArrayList<V>
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: ArrayList<V>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putCharSequenceArrayList(key ?: property.name, value as ArrayList<CharSequence>)
    }
}
fun <F : Fragment, V : CharSequence> F.argumentCharSequenceList(key: String? = null) = ArgumentCharSequenceList<F, V>(key)


class ArgumentParcelableList<F : Fragment, V : Parcelable>(key: String?) : ArgumentValue<F, ArrayList<V>>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): ArrayList<V>? {
        return fragment.arguments?.getParcelableArrayList(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: ArrayList<V>?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putParcelableArrayList(key ?: property.name, value)
    }
}
fun <F : Fragment, V : Parcelable> F.argumentParcelableList(key: String? = null) = ArgumentParcelableList<F, V>(key)


class ArgumentSize<F : Fragment>(key: String?) : ArgumentValue<F, Size>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Size? {
        return fragment.arguments?.getSize(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Size?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putSize(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentSize(key: String? = null) = ArgumentSize<F>(key)


class ArgumentSizeF<F : Fragment>(key: String?) : ArgumentValue<F, SizeF>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): SizeF? {
        return fragment.arguments?.getSizeF(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: SizeF?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putSizeF(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentSizeF(key: String? = null) = ArgumentSizeF<F>(key)


class ArgumentBinder<F : Fragment>(key: String?) : ArgumentValue<F, IBinder>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): IBinder? {
        return fragment.arguments?.getBinder(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: IBinder?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putBinder(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentBinder(key: String? = null) = ArgumentBinder<F>(key)


class ArgumentBundle<F : Fragment>(key: String?) : ArgumentValue<F, Bundle>(key) {
    override fun getValue(fragment: F, property: KProperty<*>): Bundle? {
        return fragment.arguments?.getBundle(key ?: property.name)
    }
    override fun setValue(fragment: F, property: KProperty<*>, value: Bundle?) {
        val arguments = fragment.arguments ?: Bundle().also { fragment.arguments = it }
        arguments.putBundle(key ?: property.name, value)
    }
}
fun <F : Fragment> F.argumentBundle(key: String? = null) = ArgumentBundle<F>(key)