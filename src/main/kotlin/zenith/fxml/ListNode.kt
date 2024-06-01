package zenith.fxml

import javafx.beans.DefaultProperty

@DefaultProperty("content")
class ListNode<E> : ArrayList<E>() {
    @Suppress("UNCHECKED_CAST")
    var content: Any?
        get() = null
        set(content) {
            add(content as E)
        }
}