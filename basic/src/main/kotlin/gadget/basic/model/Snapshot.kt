package gadget.basic.model

interface Snapshot<K : Comparable<K>, V : Comparable<V>> {

    val snapshotKey: K

    val snapshotValue: V
}