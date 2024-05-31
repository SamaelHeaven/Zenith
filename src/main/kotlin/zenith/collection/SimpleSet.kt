package zenith.collection

import java.util.*

class SimpleSet<E> : LinkedHashSet<E>(), SimpleCollection<E> {
    override var elements: List<E>
        get() = this
        set(value) {
            clear()
            addAll(value)
        }

    override fun get(index: Int): E {
        return ArrayList(this)[index]
    }

    override fun listIterator(): ListIterator<E> {
        return ArrayList(this).listIterator()
    }

    override fun listIterator(index: Int): ListIterator<E> {
        return ArrayList(this).listIterator(index)
    }

    override fun lastIndexOf(element: E): Int {
        return ArrayList(this).lastIndexOf(element)
    }

    override fun indexOf(element: E): Int {
        return ArrayList(this).indexOf(element)
    }

    override fun spliterator(): Spliterator<E> {
        return ArrayList(this).spliterator()
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<E> {
        return ArrayList(this).subList(fromIndex, toIndex)
    }
}