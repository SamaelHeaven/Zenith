package zenith.fxml

import javafx.beans.NamedArg
import javafx.fxml.FXMLLoader
import zenith.core.Reflection

class KeyValuePair<K, V>(@NamedArg("key") key: K, @NamedArg("value") value: V) : Map.Entry<K, V> {
    override val key: K
    override val value: V

    init {
        var actualKey = key
        var actualValue = value
        @Suppress("UNCHECKED_CAST") if (Reflection.getCallers().contains(FXMLLoader::class.java)) {
            if (key is List<*>) {
                actualKey = key.first() as K
            }
            if (value is List<*>) {
                actualValue = value.first() as V
            }
        }
        this.key = actualKey
        this.value = actualValue
    }

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