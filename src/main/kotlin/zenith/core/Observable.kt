package zenith.core

import javafx.beans.property.ObjectProperty

abstract class Observable<T> {
    internal abstract val objectProperty: ObjectProperty<T>
    abstract val value: T

    abstract fun addListener(listener: Listener<T>)
    abstract fun removeListener(listener: Listener<T>)
}