package zenith.drawable

import javafx.scene.canvas.GraphicsContext
import zenith.core.Component
import zenith.core.Property
import zenith.core.Renderer

abstract class Drawable : Component() {
    val drawModeProperty = Property(DrawMode.CAMERA)

    var drawMode: DrawMode
        get() = drawModeProperty.value
        set(value) {
            drawModeProperty.value = value
        }

    override fun update() {
        Renderer.draw(this)
    }

    abstract fun isVisible(): Boolean

    internal abstract fun draw(gc: GraphicsContext)
}