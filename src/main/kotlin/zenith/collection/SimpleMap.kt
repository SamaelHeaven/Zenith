package zenith.collection

class SimpleMap<K, V> : HashMap<K, V>(), SimpleCollection<KeyValuePair<K, V>> {
    override var elements: List<KeyValuePair<K, V>>
        get() = throw UnsupportedOperationException()
        set(_) = throw UnsupportedOperationException()

    @Suppress("UNCHECKED_CAST")
    override fun add(element: KeyValuePair<K, V>): Boolean {
        val key: K = if (element.key is List<*>) {
            element.key.first() as K
        } else {
            element.key
        }
        val value: V = if (element.value is List<*>) {
            element.value.first() as V
        } else {
            element.value
        }
        return get(key) != put(key, value)
    }

    override fun contains(element: KeyValuePair<K, V>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun containsAll(elements: Collection<KeyValuePair<K, V>>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun get(index: Int): KeyValuePair<K, V> {
        throw UnsupportedOperationException()
    }

    override fun iterator(): Iterator<KeyValuePair<K, V>> {
        throw UnsupportedOperationException()
    }

    override fun listIterator(): ListIterator<KeyValuePair<K, V>> {
        throw UnsupportedOperationException()
    }

    override fun listIterator(index: Int): ListIterator<KeyValuePair<K, V>> {
        throw UnsupportedOperationException()
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<KeyValuePair<K, V>> {
        throw UnsupportedOperationException()
    }

    override fun lastIndexOf(element: KeyValuePair<K, V>): Int {
        throw UnsupportedOperationException()
    }

    override fun indexOf(element: KeyValuePair<K, V>): Int {
        throw UnsupportedOperationException()
    }
}