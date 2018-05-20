package ir.toolki.ganmaz.classification

interface Document {
    val id: Int
    val label: String
    fun body(): String
}