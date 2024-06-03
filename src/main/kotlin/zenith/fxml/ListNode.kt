package zenith.fxml

import javafx.beans.DefaultProperty

@DefaultProperty("content")
class ListNode<E> : ArrayList<E>() {
    var content: E?
        get() = null
        set(content) {
            if (content != null) {
                add(content)
            }
        }
}