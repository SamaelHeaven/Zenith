package zenith.fxml

import javafx.beans.DefaultProperty

@DefaultProperty("content")
open class SetNode<E> : HashSet<E>() {
    open var content: E?
        get() = null
        set(content) {
            if (content != null) {
                add(content)
            }
        }
}