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
    companion object {
        fun from(entity: Entity): BoundingBox {
            return from(entity.position, entity.scale, entity.origin, entity.pivotPoint, entity.rotation)
        }

        fun from(
            position: Vector2,
            size: Vector2,
            origin: Vector2,
            pivotPoint: Vector2,
            rotation: Float
        ): BoundingBox {
            val halfSize = size * 0.5
            val originOffset = size * (origin * 0.5)
            val topLeft = position - halfSize + originOffset
            if (rotation == 0f) {
                return BoundingBox(topLeft, size)
            }
            val topRight = position + Vector2(halfSize.x, -halfSize.y) + originOffset
            val bottomLeft = position + Vector2(-halfSize.x, halfSize.y) + originOffset
            val bottomRight = position + halfSize + originOffset
            val pivot = position + pivotPoint
            val rotatedTopLeft = rotatePoint(topLeft, rotation, pivot)
            val rotatedTopRight = rotatePoint(topRight, rotation, pivot)
            val rotatedBottomLeft = rotatePoint(bottomLeft, rotation, pivot)
            val rotatedBottomRight = rotatePoint(bottomRight, rotation, pivot)
            val minX = minOf(rotatedTopLeft.x, rotatedTopRight.x, rotatedBottomLeft.x, rotatedBottomRight.x)
            val maxX = maxOf(rotatedTopLeft.x, rotatedTopRight.x, rotatedBottomLeft.x, rotatedBottomRight.x)
            val minY = minOf(rotatedTopLeft.y, rotatedTopRight.y, rotatedBottomLeft.y, rotatedBottomRight.y)
            val maxY = maxOf(rotatedTopLeft.y, rotatedTopRight.y, rotatedBottomLeft.y, rotatedBottomRight.y)
            return BoundingBox(minX, minY, maxX - minX, maxY - minY)
        }

        private fun rotatePoint(point: Vector2, angle: Float, pivot: Vector2): Vector2 {
            val rad = Math.toRadians(angle.toDouble())
            val cos = cos(rad)
            val sin = sin(rad)
            val translatedX = point.x - pivot.x
            val translatedY = point.y - pivot.y
            val rotatedX = translatedX * cos - translatedY * sin
            val rotatedY = translatedX * sin + translatedY * cos
            return Vector2(rotatedX + pivot.x, rotatedY + pivot.y)
        }
    }

    constructor(@NamedArg("position") position: Vector2, @NamedArg("size") size: Vector2) : this(
        position.x, position.y, size.x, size.y
    )

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