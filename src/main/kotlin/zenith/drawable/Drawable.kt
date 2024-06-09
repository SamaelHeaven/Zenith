package zenith.drawable

import javafx.scene.canvas.GraphicsContext
import zenith.core.Component
import zenith.core.Renderer

abstract class Drawable : Component() {
    var drawMode = DrawMode.CAMERA

    final override fun update() {
        Renderer.draw(this)
    }

    abstract fun isVisible(): Boolean

    internal abstract fun draw(gc: GraphicsContext)
}