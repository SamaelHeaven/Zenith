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
        if (drawable.renderingMode == RenderingMode.WORLD) {
            val camera = Game.scene.camera
            val translate = -camera.target + camera.offset
            val offset = Game.size / 2
            if (camera.zoom != 1f) {
                gc.translate(offset.x.toDouble(), offset.y.toDouble())
                gc.scale(camera.zoom.toDouble(), camera.zoom.toDouble())
                gc.translate(-offset.x.toDouble(), -offset.y.toDouble())
            }
            if (camera.rotation != 0f) {
                gc.translate(offset.x.toDouble(), offset.y.toDouble())
                gc.rotate(camera.rotation.toDouble())
                gc.translate(-offset.x.toDouble(), -offset.y.toDouble())
            }
            gc.translate(translate.x.toDouble(), translate.y.toDouble())
        }
        drawable.draw(gc)
        gc.restore()
    }
}