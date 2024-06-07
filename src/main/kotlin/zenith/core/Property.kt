package zenith.core

import javafx.beans.NamedArg
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener

open class Property<T>(@NamedArg("value") initialValue: T) : Observable<T>() {
    private val listeners = mutableMapOf<Listener<T>, ChangeListener<T>>()
    final override val objectProperty: ObjectProperty<T> = ObservableProperty<T>(initialValue) { set(it) }

    final override var value: T
        get() = objectProperty.get()
        set(value) {
            objectProperty.set(value)
        }

    final override fun addListener(listener: Listener<T>) {
        val changeListener = ChangeListener { _, oldValue, newValue -> listener.onChange(oldValue, newValue) }
        listeners[listener] = changeListener
        objectProperty.addListener(changeListener)
    }

    final override fun removeListener(listener: Listener<T>) {
        val foundListener = listeners[listener]
        foundListener?.let {
            objectProperty.removeListener(it)
            listeners.remove(listener)
        }
    }

    protected open fun set(value: T) {
        return (objectProperty as ObservableProperty).superSet(value)
    }

    fun toReadOnly(): ReadOnlyProperty<T> {
        return ReadOnlyProperty(this)
    }

    fun bind(property: Observable<T>) {
        objectProperty.bind(property.objectProperty)
    }

    fun unbind() {
        objectProperty.unbind()
    }

    fun bindBidirectional(property: Property<T>) {
        objectProperty.bindBidirectional(property.objectProperty)
    }

    fun unbindBidirectional(property: Property<T>) {
        objectProperty.unbindBidirectional(property.objectProperty)
    }

    private class ObservableProperty<T>(value: T, private val setter: (value: T) -> Unit) : SimpleObjectProperty<T>(value) {
        override fun set(value: T) {
            setter(value)
        }

        fun superSet(value: T) {
            super.set(value)
        }
    }
}