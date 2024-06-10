package zenith.prefab

import zenith.asset.Texture
import zenith.core.Entity
import zenith.drawable.Sprite
import zenith.math.Vector2

class SpritePrefab : DrawablePrefab<Sprite>() {
    var offset = Vector2.ZERO
    var texture: Texture? = null
    var flippedHorizontally = false
    var flippedVertically = false
    var smooth = false

    override fun buildDrawable(entity: Entity): Sprite {
        return Sprite(
            entity = entity,
            offset = offset,
            texture = texture,
            flippedHorizontally = flippedHorizontally,
            flippedVertically = flippedVertically,
            smooth = smooth,
            alpha = alpha,
            drawMode = drawMode
        )
    }
}