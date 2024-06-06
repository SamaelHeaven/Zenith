package zenith.fxml

import javafx.beans.DefaultProperty
import javafx.fxml.FXMLLoader
import zenith.core.Reflection

@DefaultProperty("content")
open class SetNode<E> : HashSet<E>() {
    var content: E?
        get() = null
        set(content) {
            if (content != null) {
                add(content)
            }
        }

    @Suppress("UNCHECKED_CAST")
    final override fun add(element: E): Boolean {
        val clazz = Reflection.getCallerClass(3) ?: return super.add(element)
        if (clazz.toString().split("$").firstOrNull() != FXMLLoader::class.java.toString()) {
            return super.add(element)
        }
        val actualElement: E = if (element is List<*>) {
            element.first() as E
        } else {
            element
        }
        return super.add(actualElement)
    }
}