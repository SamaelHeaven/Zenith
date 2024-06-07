package zenith.core

import javafx.beans.property.ObjectProperty

class ReadOnlyProperty<T>(private val property: Property<T>) : Observable<T>() {
    override val objectProperty: ObjectProperty<T> get() = property.objectProperty
    override val value: T get() = property.value

    override fun removeListener(listener: Listener<T>) {
        property.removeListener(listener)
    }

    override fun addListener(listener: Listener<T>) {
        property.addListener(listener)
    }
}