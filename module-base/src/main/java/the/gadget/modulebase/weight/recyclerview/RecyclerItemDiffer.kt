package the.gadget.modulebase.weight.recyclerview

interface RecyclerItemDiffer {

    fun getItemSnapshot(): String

    fun getContentSnapshot(): String
}