package zenith.collection

class SimpleList<E> : ArrayList<E>(), SimpleCollection<E> {
    override var elements: List<E>
        get() = throw UnsupportedOperationException()
        set(_) = throw UnsupportedOperationException()
}