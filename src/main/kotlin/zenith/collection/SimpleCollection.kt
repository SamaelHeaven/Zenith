package zenith.collection

import javafx.beans.DefaultProperty

@DefaultProperty("elements")
interface SimpleCollection<E> : List<E> {
    var elements: List<E>

    fun add(element: E): Boolean
}