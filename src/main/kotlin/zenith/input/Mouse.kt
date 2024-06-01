package zenith.input

import javafx.scene.input.MouseEvent
import javafx.scene.robot.Robot
import zenith.core.Game
import zenith.math.Vector2

object Mouse {
    private var _newPosition = Vector2.ZERO
    private var _position = Vector2.ZERO
    private var _newInsideScreen = false
    private var _insideScreen = false
    private val robot: Robot?

    var position: Vector2
        get() = _position
        set(value) {
            if (_position == value) {
                return
            }
            robot?.let {
                val clampedValue = value.clamp(Vector2.ZERO, Game.size)
                val stage = Game.fxStage
                val canvas = Game.fxCanvas
                val stagePosition = Vector2(stage.x, stage.y)
                val stageSize = Vector2(stage.width, stage.height)
                val scaledValue = clampedValue * Vector2(canvas.scaleX, canvas.scaleY)
                val position = stagePosition + (stageSize / 2) + scaledValue / 2
                try {
                    it.mouseMove(position.x.toDouble(), position.y.toDouble())
                    _position = clampedValue
                } catch (_: Exception) {}
            }
        }

    val insideScreen: Boolean
        get() = _insideScreen

    init {
        Game.throwIfUninitialized()
        robot = try {
            Robot()
        } catch (e: Exception) {
            null
        }
    }

    internal fun update() {
        updatePosition()
        updateInsideScreen()
    }

    internal fun onMouseMoved(event: MouseEvent) {
        _newPosition = Vector2(event.x, event.y)
    }

    internal fun onMouseEntered() {
        _newInsideScreen = true
    }

    internal fun onMouseExited() {
        _newInsideScreen = false
    }

    private fun updatePosition() {
        _position = _newPosition
    }

    private fun updateInsideScreen() {
        _insideScreen = _newInsideScreen
    }
}