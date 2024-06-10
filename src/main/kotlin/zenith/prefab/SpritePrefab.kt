package zenith.prefab

import zenith.asset.Texture
import zenith.core.Entity
import zenith.drawable.DrawMode
import zenith.drawable.Sprite
import zenith.math.Vector2

class SpritePrefab : Prefab() {
    var drawMode = DrawMode.CAMERA
    var offset = Vector2.ZERO
    var texture: Texture? = null
    var flippedHorizontally = false
    var flippedVertically = false
    var alpha = 1f

    override fun build(entity: Entity) {
        entity.addComponent(
            Sprite(
                entity = entity,
                offset = offset,
                texture = texture,
                flippedHorizontally = flippedHorizontally,
                flippedVertically = flippedVertically,
                alpha = alpha,
                drawMode = drawMode
            )
        )
    }
}