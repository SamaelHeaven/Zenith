package zenith.input

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.robot.Robot
import zenith.core.Game
import zenith.math.Vector2
import zenith.math.div
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

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
        robot?.let {
            val local = Game.fxCanvas.screenToLocal(robot.mouseX, robot.mouseY)
            _position = Vector2(local.x, local.y)
        }
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
    }

    private fun move(value: Vector2) {
        if (!Game.focused) {
            return
        }
        robot?.let {
            val current = Game.fxCanvas.screenToLocal(round(it.mouseX), round(it.mouseY))
            if (round(current.x) < 0 || round(current.x) > Game.width || round(current.y) < 0 || round(current.y) > Game.height) {
                return
            }
            val clampedValue = value.clamp(Vector2.ZERO, Game.size)
            val screen = Game.fxCanvas.localToScreen(round(clampedValue.x.toDouble()), round(clampedValue.y.toDouble()))
            try {
                it.mouseMove(round(screen.x), round(screen.y))
                _position = clampedValue
            } catch (_: Exception) {}
        }
    }
}