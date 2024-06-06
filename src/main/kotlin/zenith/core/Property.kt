package zenith.core

import javafx.beans.NamedArg

class Property<T>(@NamedArg("value") initialValue: T) {
    private var _value: T = initialValue
    private var notifying = false
    private val listeners = HashSet<Listener<T>>()
    private val pendingAdditions = mutableListOf<Listener<T>>()
    private val pendingRemovals = mutableListOf<Listener<T>>()

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
                listeners.addAll(pendingAdditions)
                listeners.removeAll(pendingRemovals.toSet())
                pendingAdditions.clear()
                pendingRemovals.clear()
            }
        }

    fun addListener(listener: Listener<T>) {
        if (notifying) {
            pendingAdditions.add(listener)
            return
        }
        listeners.add(listener)
    }

    fun removeListener(listener: Listener<T>) {
        if (notifying) {
            pendingRemovals.add(listener)
            return
        }
        listeners.remove(listener)
    }
}