package zenith.core

object Reflection {
    fun getCallerClass(level: Int): Class<*>? {
        val stacktrace = Thread.currentThread().stackTrace
        return try {
            Class.forName(stacktrace[level].className)
        } catch (_: Exception) {
            null
        }
    }
}