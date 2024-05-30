package zenith.core

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import zenith.paint.Color

object Renderer {
    private lateinit var graphicsContext: GraphicsContext

    init {
        Game.throwIfUninitialized()
    }

    internal fun initialize(fxCanvas: Canvas) {
        graphicsContext = fxCanvas.graphicsContext2D
    }

    fun clearBackground(color: Color) {
        graphicsContext.fill = color.fxPaint
        graphicsContext.fillRect(0.0, 0.0, Game.width.toDouble(), Game.height.toDouble())
    }
}