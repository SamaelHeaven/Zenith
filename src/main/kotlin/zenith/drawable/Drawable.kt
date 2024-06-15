package zenith.drawable

import javafx.scene.canvas.GraphicsContext
import zenith.core.Component
import zenith.core.CustomProperty
import zenith.core.Property
import zenith.core.Renderer
import kotlin.math.max
import kotlin.math.min

abstract class Drawable : Component() {
    val drawModeProperty = Property(DrawMode.CAMERA)

    val alphaProperty: Property<Float> = CustomProperty(1f) { _, value, setter ->
        setter(max(0f, min(value, 1f)))
    }

    var drawMode: DrawMode
        get() = drawModeProperty.value
        set(value) {
            drawModeProperty.value = value
        }

    var alpha: Float
        get() = alphaProperty.value
        set(value) {
            alphaProperty.value = value
        }

    override fun update() {
        Renderer.draw(this)
    }

    abstract fun isVisible(): Boolean

    internal abstract fun draw(gc: GraphicsContext)
}