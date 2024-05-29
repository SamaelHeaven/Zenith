package zenith.core

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

object Renderer {
    private lateinit var graphicsContext: GraphicsContext

    init {
        Game.throwIfUninitialized()
    }

    internal fun initialize(fxCanvas: Canvas) {
        graphicsContext = fxCanvas.graphicsContext2D
    }
}