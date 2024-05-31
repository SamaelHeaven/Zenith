package zenith.core

abstract class Scene {
    private var initialized = false

    abstract fun initialize()

    abstract fun update()

    internal fun start() {
        if (!initialized) {
            initialized = true
            initialize()
            Time.restart()
        }
    }
}