package zenith.collection

import java.util.*

class SimpleSet<E> : HashSet<E>(), SimpleCollection<E> {
    override var elements: List<E>
        get() = throw UnsupportedOperationException()
        set(_) = throw UnsupportedOperationException()

    override fun get(index: Int): E {
        throw UnsupportedOperationException()
    }

    override fun listIterator(): ListIterator<E> {
        throw UnsupportedOperationException()
    }

    override fun listIterator(index: Int): ListIterator<E> {
        throw UnsupportedOperationException()
    }

    override fun lastIndexOf(element: E): Int {
        throw UnsupportedOperationException()
    }

    override fun indexOf(element: E): Int {
        throw UnsupportedOperationException()
    }

    override fun spliterator(): Spliterator<E> {
        throw UnsupportedOperationException()
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<E> {
        throw UnsupportedOperationException()
    }
}