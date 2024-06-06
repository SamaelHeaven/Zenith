package zenith.fxml

import javafx.beans.DefaultProperty

@DefaultProperty("content")
open class ListNode<E> : ArrayList<E>() {
    open var content: E?
        get() = null
        set(content) {
            if (content != null) {
                add(content)
            }
        }
}