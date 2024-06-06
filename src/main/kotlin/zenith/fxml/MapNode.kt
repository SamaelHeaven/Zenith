package zenith.fxml

import javafx.beans.DefaultProperty
import javafx.fxml.FXMLLoader

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
        val callerLevel = 3
        val stacktrace = Thread.currentThread().stackTrace
        if (stacktrace.size <= callerLevel) {
            return super.put(key, value)
        }
        val caller = Class.forName(stacktrace[callerLevel].className).toString().split("$").firstOrNull()
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