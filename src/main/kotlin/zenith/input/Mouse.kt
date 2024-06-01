package zenith.input

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.robot.Robot
import zenith.core.Game
import zenith.math.Vector2
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
            val local = Game.fxCanvas.screenToLocal(it.mouseX, it.mouseY)
            _position = Vector2(local.x, local.y).clamp(Vector2.ZERO, Game.size)
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
            val current = Game.fxCanvas.screenToLocal(it.mouseX, it.mouseY)
            if (current.x < 0 || current.x > Game.width || current.y < 0 || current.y > Game.height) {
                return
            }
            val clampedValue = value.clamp(Vector2.ZERO, Game.size)
            val screen = Game.fxCanvas.localToScreen(round(clampedValue.x.toDouble()), round(clampedValue.y.toDouble()))
            try {
                val move = Vector2(screen.x, screen.y).round()
                val local = Game.fxCanvas.screenToLocal(screen.x, screen.y)
                if (local.x < 0 || local.x > Game.width || local.y < 0 || local.y > Game.height) {
                    return
                }
                it.mouseMove(move.x.toDouble(), move.y.toDouble())
                _position = clampedValue
            } catch (_: Exception) {}
        }
    }
}