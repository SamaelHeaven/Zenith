package zenith.input

import javafx.event.EventHandler
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
        set(value) = move(value)

    val insideScreen: Boolean
        get() = _insideScreen

    init {
        Game.throwIfUninitialized()
        Game.fxCanvas.onMouseMoved = EventHandler { onMouseMoved(it) }
        Game.fxCanvas.onMouseDragged = EventHandler { onMouseMoved(it) }
        Game.fxStage.scene.onMouseEntered = EventHandler { onMouseEntered() }
        Game.fxStage.scene.onMouseExited = EventHandler { onMouseExited() }
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

    private fun onMouseMoved(event: MouseEvent) {
        _newPosition = Vector2(event.x, event.y)
    }

    private fun onMouseEntered() {
        _newInsideScreen = true
    }

    private fun onMouseExited() {
        _newInsideScreen = false
    }

    private fun updatePosition() {
        _position = _newPosition
    }

    private fun updateInsideScreen() {
        _insideScreen = _newInsideScreen
    }

    private fun move(value: Vector2) {
        if (!_insideScreen || _position == value) {
            return
        }
        robot?.let {
            val clampedValue = value.clamp(Vector2.ZERO + 1, Game.size - 1)
            val stage = Game.fxStage
            val canvas = Game.fxCanvas
            val stagePosition = Vector2(stage.x + stage.scene.x, stage.y + stage.scene.y)
            val canvasSize = Vector2(canvas.width * canvas.scaleX, canvas.height * canvas.scaleY)
            val stageSize = Vector2(stage.scene.width, stage.scene.height)
            val scaledValue = clampedValue * Vector2(canvas.scaleX, canvas.scaleY)
            val position = stagePosition + (stageSize / 2 - canvasSize / 2) + scaledValue
            try {
                it.mouseMove(position.x.toDouble(), position.y.toDouble())
                _position = clampedValue
            } catch (_: Exception) {}
        }
    }
}