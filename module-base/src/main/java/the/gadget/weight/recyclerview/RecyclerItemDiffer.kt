package the.gadget.weight.recyclerview

interface RecyclerItemDiffer {

    fun getItemSnapshot(): String

    fun getContentSnapshot(): String
}