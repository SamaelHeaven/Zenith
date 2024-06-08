package zenith.drawable

import javafx.scene.canvas.GraphicsContext
import zenith.core.Component
import zenith.core.Renderer
import zenith.core.RenderingMode

abstract class Drawable : Component() {
    var renderingMode = RenderingMode.WORLD

    final override fun update() {
        Renderer.draw(this)
    }

    internal abstract fun draw(gc: GraphicsContext)
}