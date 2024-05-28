package org.samaelheaven.zenith.core

abstract class Scene {
    private var initialized: Boolean = false

    abstract fun initialize()

    abstract fun update()

    internal fun start() {
        if (!initialized) {
            initialized = true
            initialize()
        }
    }
}