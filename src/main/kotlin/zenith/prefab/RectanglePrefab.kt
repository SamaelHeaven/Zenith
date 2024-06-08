package zenith.prefab

import zenith.core.Entity
import zenith.core.RenderingMode
import zenith.drawable.Rectangle
import zenith.paint.Color
import zenith.paint.Paint

class RectanglePrefab : Prefab() {
    var fill: Paint = Color.TRANSPARENT
    var stroke: Paint = Color.TRANSPARENT
    var strokeWidth: Float = 0.0f
    var renderingMode = RenderingMode.WORLD

    override fun build(entity: Entity) {
        entity.addComponent(
            Rectangle(
                entity = entity, fill = fill, stroke = stroke, strokeWidth = strokeWidth, renderingMode = renderingMode
            )
        )
    }
}