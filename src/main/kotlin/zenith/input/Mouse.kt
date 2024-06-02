package zenith.input

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.robot.Robot
import zenith.core.Game
import zenith.math.Vector2

object Mouse {
    private var _newPosition: Vector2? = null
    private var _position = Vector2.ZERO
    private val robot: Robot?

    val position: Vector2
        get() = _position

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
}