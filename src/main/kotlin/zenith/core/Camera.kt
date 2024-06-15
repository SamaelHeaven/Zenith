package zenith.core

import zenith.math.Vector2
import kotlin.math.max

class Camera {
    val targetProperty = Property(Vector2.ZERO)
    val offsetProperty = Property(Vector2.ZERO)
    val rotationProperty = Property(0f)

    val zoomProperty: Property<Float> = CustomProperty(1f) { _, value, setter ->
        setter(max(0f, value))
    }

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