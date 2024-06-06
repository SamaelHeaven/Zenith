package zenith.core

fun interface Listener<T> {
    fun onChange(oldValue: T, newValue: T)
}