package org.samaelheaven.zenith.core

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

class Renderer private constructor() {
    private lateinit var graphicsContext: GraphicsContext

    companion object {
        private var instance: Renderer? = null
        private val renderer: Renderer
            get() = (if (instance == null) Renderer() else instance).also { instance = it }!!

        internal fun initialize(fxCanvas: Canvas) {
            renderer.graphicsContext = fxCanvas.graphicsContext2D
        }
    }

    init {
        Game.throwIfUninitialized()
    }
}