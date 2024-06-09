package zenith.prefab

import zenith.core.Entity
import zenith.drawable.DrawMode
import zenith.drawable.Rectangle
import zenith.math.Vector2
import zenith.paint.Color
import zenith.paint.Paint

class RectanglePrefab : Prefab() {
    var drawMode = DrawMode.CAMERA
    var offset = Vector2.ZERO
    var fill: Paint = Color.TRANSPARENT
    var stroke: Paint = Color.TRANSPARENT
    var strokeWidth = 0.0f
    var radius = 0f

    override fun build(entity: Entity) {
        entity.addComponent(
            Rectangle(
                entity = entity,
                offset = offset,
                fill = fill,
                stroke = stroke,
                strokeWidth = strokeWidth,
                radius = radius,
                drawMode = drawMode
            )
        )
    }
}