package zenith.core

object Reflection {
    fun getCaller(level: Int): Class<*>? {
        val stacktrace = Thread.currentThread().stackTrace
        return try {
            Class.forName(stacktrace[level].className)
        } catch (_: Exception) {
            null
        }
    }

    fun getCallers(): List<Class<*>> {
        val result = mutableListOf<Class<*>>()
        val stacktrace = Thread.currentThread().stackTrace
        for (element in stacktrace) {
            try {
                result.add(Class.forName(element.className))
            } catch (_: Exception) {
                continue
            }
        }
        return result
    }
}