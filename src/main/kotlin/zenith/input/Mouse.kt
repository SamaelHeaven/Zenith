package zenith.input

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import zenith.core.Game
import zenith.math.Vector2
import java.awt.MouseInfo
import java.awt.Robot
import kotlin.math.max
import kotlin.math.roundToInt

object Mouse {
    private var _newPosition: Vector2? = null
    private var _position = Vector2.ZERO
    private val robot: Robot?

    var position: Vector2
        get() = _position
        set(value) = move(value)

    init {
        Game.throwIfUninitialized()
        Game.fxCanvas.onMouseMoved = EventHandler { onMouseMoved(it) }
        Game.fxCanvas.onMouseDragged = EventHandler { onMouseMoved(it) }
        robot = try {
            Robot()
        } catch (e: Exception) {
            null
        }
        updatePosition()
    }

    internal fun update() {
        updatePosition()
    }

    private fun onMouseMoved(event: MouseEvent) {
        _newPosition = Vector2(event.x, event.y)
    }

    private fun updatePosition() {
        _newPosition?.let {
            _position = it
            _newPosition = null
        }
        try {
            val point = MouseInfo.getPointerInfo().location
            val local = Game.fxCanvas.screenToLocal(point.x.toDouble(), point.y.toDouble())
            _position = Vector2(local.x, local.y).clamp(Vector2.ZERO, Game.size)
        } catch (_: Exception) {
            return
        }
    }

    private fun move(value: Vector2) {
        try {
            val point = MouseInfo.getPointerInfo().location
            val local = Game.fxCanvas.screenToLocal(point.x.toDouble(), point.y.toDouble())
            val scale = Vector2(Game.fxCanvas.scaleX, Game.fxCanvas.scaleY)
            val maxScale = max(1 / scale.x, 1 / scale.y)
            if (local.x < -maxScale || local.x > Game.width + maxScale || local.y < -maxScale || local.y > Game.height + maxScale) {
                return
            }
        } catch (_: Exception) {
            return
        }
        robot?.let {
            val clampedValue = value.clamp(Vector2.ZERO, Game.size)
            val screen = Game.fxCanvas.localToScreen(clampedValue.x.toDouble(), clampedValue.y.toDouble())
            it.mouseMove(screen.x.roundToInt(), screen.y.roundToInt())
            _position = clampedValue
        }
    }
}