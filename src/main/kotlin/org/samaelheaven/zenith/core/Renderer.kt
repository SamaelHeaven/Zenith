package org.samaelheaven.zenith.core

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Paint

class Renderer private constructor() {
    private lateinit var graphicsContext: GraphicsContext

    companion object {
        private var instance: Renderer? = null
        private val renderer: Renderer
            get() = (if (instance == null) Renderer() else instance).also { instance = it }!!

        fun clearBackground(paint: Paint) {
            renderer.graphicsContext.fill = paint
            renderer.graphicsContext.fillRect(0.0, 0.0, Game.width.toDouble(), Game.height.toDouble())
        }

        internal fun initialize(fxCanvas: Canvas) {
            renderer.graphicsContext = fxCanvas.graphicsContext2D
        }
    }

    init {
        Game.throwIfUninitialized()
    }
}