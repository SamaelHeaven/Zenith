package zenith.fxml

import javafx.beans.DefaultProperty

@DefaultProperty("content")
class SetNode<E> : HashSet<E>() {
    @Suppress("UNCHECKED_CAST")
    var content: Any?
        get() = null
        set(content) {
            add(content as E)
        }
}