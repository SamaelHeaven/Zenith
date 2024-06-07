package zenith.core

import javafx.beans.NamedArg
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener

class Property<T>(@NamedArg("value") initialValue: T, private inline val setter: (value: T) -> T = { it }) : Observable<T>() {
    private val listeners = mutableMapOf<Listener<T>, ChangeListener<T>>()
    override val objectProperty = SimpleObjectProperty(initialValue)

    override var value: T
        get() = objectProperty.get()
        set(value) {
            objectProperty.set(setter(value))
        }

    override fun addListener(listener: Listener<T>) {
        val changeListener = ChangeListener { _, oldValue, newValue -> listener.onChange(oldValue, newValue) }
        listeners[listener] = changeListener
        objectProperty.addListener(changeListener)
    }

    override fun removeListener(listener: Listener<T>) {
        val foundListener = listeners[listener]
        foundListener?.let {
            objectProperty.removeListener(it)
            listeners.remove(listener)
        }
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
}