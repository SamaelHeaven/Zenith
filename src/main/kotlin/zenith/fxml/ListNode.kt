package zenith.fxml

import javafx.beans.DefaultProperty

@DefaultProperty("content")
open class ListNode<E> : ArrayList<E>() {
    var content: E?
        get() = null
        set(content) {
            if (content != null) {
                add(content)
            }
        }

    var contents: List<E>?
        get() = null
        set(contents) {
            if (contents != null) {
                addAll(contents)
            }
        }
}