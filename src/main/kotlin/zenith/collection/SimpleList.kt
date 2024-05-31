package zenith.collection

class SimpleList<E> : ArrayList<E>(), SimpleCollection<E> {
    override var elements: List<E>
        get() = this
        set(value) {
            clear()
            addAll(value)
        }
}