package zenith.core

import zenith.math.Vector2

class Camera {
    val positionProperty = Property(Vector2.ZERO)

    var position: Vector2
        get() = positionProperty.value
        set(value) {
            positionProperty.value = value
        }
}