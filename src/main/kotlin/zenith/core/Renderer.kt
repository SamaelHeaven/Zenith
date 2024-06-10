package zenith.core

import javafx.scene.CacheHint
import javafx.scene.canvas.GraphicsContext
import zenith.drawable.Drawable
import zenith.paint.Paint

object Renderer {
    private val gc: GraphicsContext

    init {
        Game.throwIfUninitialized()
        Game.fxCanvas.isCache = true
        Game.fxCanvas.cacheHint = CacheHint.SPEED
        gc = Game.fxCanvas.graphicsContext2D
    }

    fun clearBackground(paint: Paint) {
        gc.save()
        gc.fill = paint.fxPaint
        gc.fillRect(0.0, 0.0, Game.width.toDouble(), Game.height.toDouble())
        gc.restore()
    }

    fun draw(drawable: Drawable) {
        gc.save()
        gc.transform = drawable.drawMode.transform
        gc.globalAlpha = drawable.alpha.toDouble()
        drawable.draw(gc)
        gc.restore()
    }
}