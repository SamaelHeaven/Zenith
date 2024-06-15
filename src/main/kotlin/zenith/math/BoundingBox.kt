package zenith.math

import javafx.beans.NamedArg

data class BoundingBox(
    @NamedArg("x") val x: Float,
    @NamedArg("y") val y: Float,
    @NamedArg("width") val width: Float,
    @NamedArg("height") val height: Float
) {
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