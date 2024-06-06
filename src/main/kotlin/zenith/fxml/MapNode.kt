package zenith.fxml

import javafx.beans.DefaultProperty
import javafx.fxml.FXMLLoader
import zenith.core.Reflection

@DefaultProperty("content")
open class MapNode<K, V> : HashMap<K, V>() {
    var content: KeyValuePair<K, V>?
        get() = null
        set(content) {
            if (content is KeyValuePair<*, *>) {
                super.put(content.key, content.value)
            }
        }
}