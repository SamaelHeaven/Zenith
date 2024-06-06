package zenith.core

import javafx.scene.canvas.GraphicsContext
import zenith.paint.Paint

object Renderer {
    private val graphicsContext: GraphicsContext

    init {
        Game.throwIfUninitialized()
        graphicsContext = Game.fxCanvas.graphicsContext2D
    }

    fun clearBackground(paint: Paint) {
        graphicsContext.fill = paint.fxPaint
        graphicsContext.fillRect(0.0, 0.0, Game.width.toDouble(), Game.height.toDouble())
    }
}