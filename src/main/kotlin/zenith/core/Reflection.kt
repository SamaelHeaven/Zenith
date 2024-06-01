package zenith.core

internal object Reflection {
    fun getCallerClass(level: Int): Class<*> {
        val stElements = Thread.currentThread().stackTrace
        val rawFQN = stElements[level + 1].toString().split("\\(".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()[0]
        println(rawFQN.substring(0, rawFQN.lastIndexOf('.')))
        return Class.forName(rawFQN.substring(0, rawFQN.lastIndexOf('.')))
    }
}