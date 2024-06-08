package zenith.core

import zenith.math.Vector2
import kotlin.math.max

class Camera {
    val targetProperty = Property(Vector2.ZERO)
    val offsetProperty = Property(Vector2.ZERO)
    val zoomProperty = object : Property<Float>(1f) {
        override fun set(value: Float) {
            super.set(max(0f, value))
        }
    }
    val rotationProperty = Property(0f)

    var target: Vector2
        get() = targetProperty.value
        set(value) {
            targetProperty.value = value
        }

    var offset: Vector2
        get() = offsetProperty.value
        set(value) {
            offsetProperty.value = value
        }

    var zoom: Float
        get() = zoomProperty.value
        set(value) {
            zoomProperty.value = value
        }

    var rotation: Float
        get() = rotationProperty.value
        set(value) {
            rotationProperty.value = value
        }
}