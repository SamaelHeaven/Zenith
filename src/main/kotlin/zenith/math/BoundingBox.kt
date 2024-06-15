package zenith.math

import javafx.beans.NamedArg
import zenith.core.Entity
import kotlin.math.cos
import kotlin.math.sin

data class BoundingBox(
    @NamedArg("x") val x: Float,
    @NamedArg("y") val y: Float,
    @NamedArg("width") val width: Float,
    @NamedArg("height") val height: Float
) {
    val position get() = Vector2(x, y)
    val size get() = Vector2(width, height)

    constructor(@NamedArg("position") position: Vector2, @NamedArg("size") size: Vector2) : this(
        position.x, position.y, size.x, size.y
    )

    companion object {
        fun from(entity: Entity): BoundingBox {
            return from(entity.position, entity.scale, entity.origin, entity.pivotPoint, entity.rotation)
        }

        fun from(
            position: Vector2, size: Vector2, origin: Vector2, pivotPoint: Vector2, rotation: Float
        ): BoundingBox {
            val topLeft = position - (size * 0.5 + size * (origin * 0.5))
            if (rotation == 0f) {
                return BoundingBox(topLeft, size)
            }
            val topRight = topLeft + Vector2.RIGHT * size
            val bottomLeft = topLeft + Vector2.DOWN * size
            val bottomRight = topLeft + size
            val rotationPoint = (topLeft + size * 0.5) + pivotPoint
            val rotatedTopLeft = topLeft.rotate(rotation, rotationPoint)
            val rotatedTopRight = topRight.rotate(rotation, rotationPoint)
            val rotatedBottomLeft = bottomLeft.rotate(rotation, rotationPoint)
            val rotatedBottomRight = bottomRight.rotate(rotation, rotationPoint)
            val minX = minOf(rotatedTopLeft.x, rotatedTopRight.x, rotatedBottomLeft.x, rotatedBottomRight.x)
            val maxX = maxOf(rotatedTopLeft.x, rotatedTopRight.x, rotatedBottomLeft.x, rotatedBottomRight.x)
            val minY = minOf(rotatedTopLeft.y, rotatedTopRight.y, rotatedBottomLeft.y, rotatedBottomRight.y)
            val maxY = maxOf(rotatedTopLeft.y, rotatedTopRight.y, rotatedBottomLeft.y, rotatedBottomRight.y)
            return BoundingBox(minX, minY, maxX - minX, maxY - minY)
        }
    }

    fun intersect(other: BoundingBox): Boolean {
        val thisRight = x + width
        val thisBottom = y + height
        val otherRight = other.x + other.width
        val otherBottom = other.y + other.height
        return x < otherRight && thisRight > other.x && y < otherBottom && thisBottom > other.y
    }

    fun contains(position: Vector2): Boolean {
        return position.x >= x && position.x <= x + width && position.y >= y && position.y <= y + height
    }

    override fun equals(other: Any?): Boolean {
        if (other is BoundingBox) {
            return x == other.x && y == other.y && width == other.width && height == other.height
        }
        return false
    }

    override fun toString(): String {
        return "{x: $x, y: $y, width: $width, height: $height}"
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        return result
    }
}