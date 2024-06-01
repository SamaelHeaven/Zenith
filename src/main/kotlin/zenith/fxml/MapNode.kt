package zenith.fxml

import javafx.beans.DefaultProperty
import javafx.fxml.FXMLLoader
import zenith.core.Reflection

@DefaultProperty("content")
class MapNode<K, V> : HashMap<K, V>() {
    var content: List<KeyValuePair<K, V>>
        get() = null!!
        set(_) {}

    @Suppress("UNCHECKED_CAST")
    override fun put(key: K, value: V): V? {
        val caller = Reflection.getCallerClass(2).toString().split("$").firstOrNull()
        if (caller.toString() != FXMLLoader::class.java.toString()) {
            return super.put(key, value)
        }
        val keyValuePair = value as KeyValuePair<K, V>
        val actualKey: K = if (keyValuePair.key is List<*>) {
            keyValuePair.key.first() as K
        } else {
            keyValuePair.key
        }
        val actualValue: V = if (keyValuePair.value is List<*>) {
            keyValuePair.value.first() as V
        } else {
            keyValuePair.value
        }
        return super.put(actualKey, actualValue)
    }
}