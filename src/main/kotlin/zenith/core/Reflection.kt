package zenith.core

internal object Reflection {
    fun getCallerClass(level: Int): Class<*> {
        println(Thread.currentThread().stackTrace[level].className)
        return Class.forName(Thread.currentThread().stackTrace[level].className)
    }
}