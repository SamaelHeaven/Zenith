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

    @Suppress("UNCHECKED_CAST")
    final override fun put(key: K, value: V): V? {
        if (!Reflection.getCallers().contains(FXMLLoader::class.java)) {
            return super.put(key, value)
        }
        val keyValuePair = value as KeyValuePair<K, V>
        return super.put(keyValuePair.key, keyValuePair.value)
    }
}