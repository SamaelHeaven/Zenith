package zenith.collection

class SimpleMap<K, V> : LinkedHashMap<K, V>(), SimpleCollection<KeyValuePair<Collection<K>, Collection<V>>> {
    override var elements: List<KeyValuePair<Collection<K>, Collection<V>>>
        get() = this
        set(value) {
            clear()
            for (element in value) {
                put(element.key.first(), element.value.first())
            }
        }

    @Suppress("UNCHECKED_CAST")
    override fun add(element: KeyValuePair<Collection<K>, Collection<V>>): Boolean {
        val key: Collection<K> = if (element.key is List<*>) {
            element.key
        } else {
            listOf(element.key as K)
        }
        val value: Collection<V> = if (element.value is List<*>) {
            element.value
        } else {
            listOf(element.value as V)
        }
        val oldValue = get(key.first())
        return put(key.first(), value.first()) != oldValue
    }

    override fun contains(element: KeyValuePair<Collection<K>, Collection<V>>): Boolean {
        return toList().contains(element)
    }

    override fun containsAll(elements: Collection<KeyValuePair<Collection<K>, Collection<V>>>): Boolean {
        return toList().containsAll(elements)
    }

    override fun get(index: Int): KeyValuePair<Collection<K>, Collection<V>> {
        return toList()[index]
    }

    override fun iterator(): Iterator<KeyValuePair<Collection<K>, Collection<V>>> {
        return toList().iterator()
    }

    override fun listIterator(): ListIterator<KeyValuePair<Collection<K>, Collection<V>>> {
        return toList().listIterator()
    }

    override fun listIterator(index: Int): ListIterator<KeyValuePair<Collection<K>, Collection<V>>> {
        return toList().listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<KeyValuePair<Collection<K>, Collection<V>>> {
        return toList().subList(fromIndex, toIndex)
    }

    override fun lastIndexOf(element: KeyValuePair<Collection<K>, Collection<V>>): Int {
        return toList().lastIndexOf(element)
    }

    override fun indexOf(element: KeyValuePair<Collection<K>, Collection<V>>): Int {
        return toList().indexOf(element)
    }

    private fun toList(): List<KeyValuePair<Collection<K>, Collection<V>>> {
        val result = ArrayList<KeyValuePair<Collection<K>, Collection<V>>>()
        val entries = entries
        for (entry in entries) {
            val key = listOf(entry.key)
            val value = listOf(entry.value)
            result.add(KeyValuePair(key, value))
        }
        return result
    }
}