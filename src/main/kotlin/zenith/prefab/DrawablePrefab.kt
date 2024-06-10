package zenith.prefab

import zenith.core.Entity
import zenith.drawable.DrawMode
import zenith.drawable.Drawable

abstract class DrawablePrefab<T : Drawable> : Prefab() {
    var drawMode = DrawMode.CAMERA
    var alpha = 1f

    protected abstract fun buildDrawable(entity: Entity): T

    override fun build(entity: Entity) {
        entity.addComponent(buildDrawable(entity))
    }
}