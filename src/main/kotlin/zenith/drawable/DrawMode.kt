package zenith.drawable

import javafx.scene.transform.Affine
import zenith.core.Entity
import zenith.core.Game
import zenith.math.Vector2

enum class DrawMode {
    SCREEN {
        override val transform: Affine
            get() {
                return Affine()
            }
    },
    CAMERA {
        override val transform: Affine
            get() {
                val camera = Game.scene.camera
                val translate = -camera.target + camera.offset
                val offset = Game.size * 0.5
                val transform = Affine()
                if (camera.zoom != 1f) {
                    transform.appendScale(
                        camera.zoom.toDouble(), camera.zoom.toDouble(), offset.x.toDouble(), offset.y.toDouble()
                    )
                }
                if (camera.rotation != 0f) {
                    transform.appendRotation(camera.rotation.toDouble(), offset.x.toDouble(), offset.y.toDouble())
                }
                transform.appendTranslation(translate.x.toDouble(), translate.y.toDouble())
                return transform
            }
    };

    fun isVisible(entity: Entity): Boolean {
        val topLeft = entity.position - (entity.scale * 0.5 + entity.scale * (entity.origin * 0.5))
        val rotationPoint = (topLeft + entity.scale * 0.5) + entity.pivotPoint
        return isVisible(transform, topLeft, entity.scale, rotationPoint, entity.rotation)
    }

    fun isVisible(
        position: Vector2,
        size: Vector2,
        origin: Vector2 = Vector2.ZERO,
        pivotPoint: Vector2 = Vector2.ZERO,
        rotation: Float = 0f
    ): Boolean {
        val topLeft = position - (size * 0.5 + size * (origin * 0.5))
        val rotationPoint = (topLeft + size * 0.5) + pivotPoint
        return isVisible(transform, topLeft, size, rotationPoint, rotation)
    }

    internal abstract val transform: Affine

    companion object {
        internal fun isVisible(
            transform: Affine, position: Vector2, size: Vector2, rotationPoint: Vector2, rotation: Float
        ): Boolean {
            val points = listOf(
                position, position + Vector2(size.x, 0f), position + size, position + Vector2(0f, size.y)
            )
            val rotatedPoints = if (rotation != 0f) {
                points.map { point -> point.rotate(rotation, rotationPoint) }
            } else {
                points
            }
            val transformedPoints = rotatedPoints.map {
                transform.transform(it.x.toDouble(), it.y.toDouble())
            }
            val minX = transformedPoints.minOf { it.x }
            val maxX = transformedPoints.maxOf { it.x }
            val minY = transformedPoints.minOf { it.y }
            val maxY = transformedPoints.maxOf { it.y }
            return maxX >= 0 && minX <= Game.width && maxY >= 0 && minY <= Game.height
        }
    }
}