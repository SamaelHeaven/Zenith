package zenith.core

import zenith.math.Vector2

class Camera {
    val positionObservable = Observable(Vector2.ZERO)

    var position: Vector2
        get() = positionObservable.value
        set(value) {
            positionObservable.value = value
        }
}