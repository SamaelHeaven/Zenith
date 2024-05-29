package zenith.math

import javafx.beans.NamedArg
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector2 {
    val x: Float
    val y: Float

    companion object {
        val ZERO = Vector2()
        val UP = Vector2(0, -1)
        val DOWN = Vector2(0, 1)
        val LEFT = Vector2(-1, 0)
        val RIGHT = Vector2(1, 0)

        @JvmStatic
        fun valueOf(value: String): Vector2 {
            val scanner = Scanner(value)
            val x = if (scanner.hasNextFloat()) scanner.nextFloat() else 0
            val y = if (scanner.hasNextFloat()) scanner.nextFloat() else x
            return Vector2(x, y)
        }
    }

    constructor(@NamedArg("x") x: Number, @NamedArg("y") y: Number) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    constructor(@NamedArg("xy") xy: Number) : this(xy, xy)

    constructor() : this(0)

    operator fun plus(v: Vector2) = Vector2(x + v.x, y + v.y)

    operator fun minus(v: Vector2) = Vector2(x - v.x, y - v.y)

    operator fun times(v: Vector2) = Vector2(x * v.x, y * v.y)

    operator fun div(v: Vector2) = Vector2(x / v.x, y / v.y)

    operator fun plus(f: Number) = Vector2(x + f.toFloat(), y + f.toFloat())

    operator fun minus(f: Number) = Vector2(x - f.toFloat(), y - f.toFloat())

    operator fun times(f: Number) = Vector2(x * f.toFloat(), y * f.toFloat())

    operator fun div(f: Number) = Vector2(x / f.toFloat(), y / f.toFloat())

    operator fun unaryMinus() = Vector2(-x, -y)

    operator fun component1(): Float = x

    operator fun component2(): Float = y

    fun isInside(min: Vector2, max: Vector2): Boolean {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y
    }

    fun clamp(min: Vector2, max: Vector2): Vector2 {
        return clampX(min.x, max.x).clampY(min.y, max.y)
    }

    fun clamp(min: Number, max: Number): Vector2 {
        return clampX(min, max).clampY(min, max)
    }

    fun clampX(min: Number, max: Number): Vector2 {
        if (x < min.toFloat()) {
            return Vector2(min, y)
        }
        return if (x > max.toFloat()) Vector2(max, y) else this
    }

    fun clampY(min: Number, max: Number): Vector2 {
        if (y < min.toFloat()) {
            return Vector2(x, min)
        }
        return if (y > max.toFloat()) Vector2(x, max) else this
    }

    fun distanceTo(v: Vector2): Float {
        val dx = v.x - x
        val dy = v.y - y
        return sqrt((dx * dx + dy * dy))
    }

    fun dot(v: Vector2): Float {
        return x * v.x + y * v.y
    }

    fun cross(v: Vector2): Float {
        return x * v.y - y * v.x
    }

    fun angleBetween(v: Vector2): Float {
        return acos(dot(v) / (length() * v.length()))
    }

    fun reflect(v: Vector2): Vector2 {
        val dot = dot(v)
        return Vector2(x - 2 * dot * v.x, y - 2 * dot * v.y)
    }

    fun lerp(end: Vector2, t: Number): Vector2 {
        val result = 0f.coerceAtLeast(1f.coerceAtMost(t.toFloat()))
        return Vector2(x + (end.x - x) * result, y + (end.y - y) * result)
    }

    fun slerp(end: Vector2, t: Number): Vector2 {
        val angle = angleBetween(end)
        val x = x * cos((angle * t.toFloat())) + end.x * sin((angle * t.toFloat()))
        val y = y * cos(angle * t.toFloat()) + end.y * sin(angle * t.toFloat())
        return Vector2(x, y)
    }

    fun length(): Float {
        return sqrt(x * x + y * y)
    }

    fun round(): Vector2 {
        return Vector2(Math.round(x), Math.round(y))
    }

    fun abs(): Vector2 {
        return Vector2(kotlin.math.abs(x), kotlin.math.abs(y))
    }

    fun normalize(): Vector2 {
        val length = length()
        return if (length != 0f) Vector2(x / length, y / length) else ZERO
    }

    override fun toString(): String {
        return "{ \"x\": $x, \"y\": $y}"
    }

    override fun equals(other: Any?): Boolean {
        if (other is Vector2) {
            return x == other.x && y == other.y
        }
        return false
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}