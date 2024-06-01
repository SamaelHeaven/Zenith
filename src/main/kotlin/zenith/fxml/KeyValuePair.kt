package zenith.fxml

import javafx.beans.NamedArg

class KeyValuePair<K, V>(@NamedArg("key") override val key: K, @NamedArg("value") override val value: V) : Map.Entry<K, V> {
    operator fun component1() = key
    operator fun component2() = value

    override fun toString(): String {
        return "{key: $key, value: $value}"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Map.Entry<*, *>) {
            return key == other.key && value == other.value
        }
        return false
    }

    override fun hashCode(): Int {
        var result = key?.hashCode() ?: 0
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }
}