package zenith.fxml

import javafx.beans.DefaultProperty

@DefaultProperty("factory")
internal interface Wrapper<T> {
    @Suppress("UNCHECKED_CAST")
    var factory: Any
        get() = throw UnsupportedOperationException()
        set(value) {
            handle(value as T)
        }

    fun handle(value: T)
}