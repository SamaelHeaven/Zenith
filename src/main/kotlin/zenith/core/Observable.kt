package zenith.core

import javafx.beans.NamedArg

class Observable<T>(@NamedArg("value") initialValue: T) {
    private var _value: T = initialValue
    private var notifying = false
    private val listeners = mutableSetOf<Listener<T>>()
    private val listenersToAdd = mutableListOf<Listener<T>>()
    private val listenersToRemove = mutableListOf<Listener<T>>()
    private var boundObservable: Observable<T>? = null
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

    fun bind(observable: Observable<T>) {
        boundObservable?.let { unbind() }
        boundObservable = observable
        val listener = Listener<T> { _, newValue -> value = newValue }
        observable.addListener(listener)
        boundListener = listener
        this.value = observable.value
    }

    fun unbind() {
        boundObservable?.removeListener(boundListener!!)
        boundObservable = null
    }

    fun bindBidirectional(other: Observable<T>) {
        boundObservable?.let { unbind() }
        other.boundObservable?.let { other.unbind() }
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

    private class BidirectionalBinding<T>(val observable1: Observable<T>, val observable2: Observable<T>) {
        private val listener1 = Listener<T> { _, newValue ->
            if (observable2.value != newValue) {
                observable2.value = newValue
            }
        }

        private val listener2 = Listener<T> { _, newValue ->
            if (observable1.value != newValue) {
                observable1.value = newValue
            }
        }

        init {
            observable1.addListener(listener1)
            observable2.addListener(listener2)
            observable1.value = observable2.value
        }

        fun unbind() {
            observable1.removeListener(listener1)
            observable2.removeListener(listener2)
        }
    }
}