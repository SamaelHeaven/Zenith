package zenith.core

import javafx.beans.NamedArg

class Property<T>(@NamedArg("value") initialValue: T) {
    private var _value: T = initialValue
    private var notifying = false
    private val listeners = mutableSetOf<Listener<T>>()
    private val listenersToAdd = mutableListOf<Listener<T>>()
    private val listenersToRemove = mutableListOf<Listener<T>>()
    private var boundProperty: Property<T>? = null
    private var boundListener: Listener<T>? = null
    private var bidirectionalBinding: BidirectionalBinding<T>? = null

    var value: T
        get() = _value
        set(value) {
            if (_value == value) {
                return
            }
            val oldValue = _value
            _value = value
            notifying = true
            try {
                for (listener in listeners) {
                    listener.onChange(oldValue, _value)
                }
            } finally {
                notifying = false
                listeners.addAll(listenersToAdd)
                listeners.removeAll(listenersToRemove.toSet())
                listenersToAdd.clear()
                listenersToRemove.clear()
            }
        }

    fun addListener(listener: Listener<T>) {
        if (notifying) {
            listenersToAdd.add(listener)
            return
        }
        listeners.add(listener)
    }

    fun removeListener(listener: Listener<T>) {
        if (notifying) {
            listenersToRemove.add(listener)
            return
        }
        listeners.remove(listener)
    }

    fun bind(property: Property<T>) {
        boundProperty?.let { unbind() }
        boundProperty = property
        val listener = Listener<T> { _, newValue -> value = newValue }
        property.addListener(listener)
        boundListener = listener
        this.value = property.value
    }

    fun unbind() {
        boundProperty?.removeListener(boundListener!!)
        boundProperty = null
    }

    fun bindBidirectional(other: Property<T>) {
        bidirectionalBinding?.let { unbindBidirectional() }
        other.bidirectionalBinding?.let { other.unbindBidirectional() }
        val binding = BidirectionalBinding(this, other)
        this.bidirectionalBinding = binding
        other.bidirectionalBinding = binding
    }

    fun unbindBidirectional() {
        bidirectionalBinding?.unbind()
        bidirectionalBinding = null
    }

    private class BidirectionalBinding<T>(val property1: Property<T>, val property2: Property<T>) {
        private val listener1 = Listener<T> { _, newValue ->
            if (property2.value != newValue) {
                property2.value = newValue
            }
        }

        private val listener2 = Listener<T> { _, newValue ->
            if (property1.value != newValue) {
                property1.value = newValue
            }
        }

        init {
            property1.addListener(listener1)
            property2.addListener(listener2)
            property1.value = property2.value
        }

        fun unbind() {
            property1.removeListener(listener1)
            property2.removeListener(listener2)
        }
    }
}