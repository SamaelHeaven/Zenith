package zenith.input

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import zenith.core.Game
import zenith.math.Vector2
import java.awt.MouseInfo
import java.awt.Robot
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

object Mouse {
    private val newPressedButtons = mutableSetOf<MouseButton>()
    private val newReleasedButtons = mutableSetOf<MouseButton>()
    private val newClickedButtons = mutableSetOf<MouseButton>()
    private var newPosition: Vector2? = null
    private var newScroll = Vector2.ZERO
    private var _position = Vector2.ZERO
    private val _downButtons = mutableSetOf<MouseButton>()
    private val _upButtons = mutableSetOf<MouseButton>()
    private val _pressedButtons = mutableSetOf<MouseButton>()
    private val _releasedButtons = mutableSetOf<MouseButton>()
    private val _clickedButtons = mutableSetOf<MouseButton>()
    private var _scroll = Vector2.ZERO
    private val robot: Robot?
    val downButtons: Set<MouseButton> get() = Collections.unmodifiableSet(_downButtons)
    val upButtons: Set<MouseButton> get() = Collections.unmodifiableSet(_upButtons)
    val pressedButtons: Set<MouseButton> get() = Collections.unmodifiableSet(_pressedButtons)
    val releasedButtons: Set<MouseButton> get() = Collections.unmodifiableSet(_releasedButtons)
    val clickedButtons: Set<MouseButton> get() = Collections.unmodifiableSet(_clickedButtons)
    val scroll: Vector2 get() = _scroll

    var position: Vector2
        get() = _position
        set(value) = move(value)

    init {
        Game.throwIfUninitialized()
        Game.fxCanvas.onMouseMoved = EventHandler { onMouseMoved(it) }
        Game.fxCanvas.onMouseDragged = EventHandler { onMouseMoved(it) }
        Game.fxCanvas.onMousePressed = EventHandler { onMousePressed(it) }
        Game.fxCanvas.onMouseReleased = EventHandler { onMouseReleased(it) }
        Game.fxCanvas.onMouseClicked = EventHandler { onMouseClicked(it) }
        Game.fxCanvas.onScroll = EventHandler { onScroll(it) }
        robot = try {
            Robot()
        } catch (e: Exception) {
            null
        }
        updatePosition()
    }

    fun isButtonDown(button: MouseButton): Boolean {
        return _downButtons.contains(button)
    }

    fun isButtonUp(button: MouseButton): Boolean {
        return _upButtons.contains(button)
    }

    fun isButtonPressed(button: MouseButton): Boolean {
        return _pressedButtons.contains(button)
    }

    fun isButtonReleased(button: MouseButton): Boolean {
        return _releasedButtons.contains(button)
    }

    fun isButtonClicked(button: MouseButton): Boolean {
        return _clickedButtons.contains(button)
    }

    internal fun update() {
        updatePosition()
        if (!Game.focused) {
            reset()
            return
        }
        updatePressedButtons()
        updateDownButtons()
        updateReleasedButtons()
        updateUpButtons()
        updateClickedButtons()
        updateScroll()
    }

    private fun onMouseMoved(event: MouseEvent) {
        newPosition = Vector2(event.x, event.y)
    }

    private fun onMousePressed(event: MouseEvent) {
        newPressedButtons.add(MouseButton.get(event.button))
    }

    private fun onMouseReleased(event: MouseEvent) {
        newReleasedButtons.add(MouseButton.get(event.button))
    }

    private fun onMouseClicked(event: MouseEvent) {
        newClickedButtons.add(MouseButton.get(event.button))
    }

    private fun onScroll(event: ScrollEvent) {
        val x = when {
            event.deltaX > 0 -> 1
            event.deltaX < 0 -> -1
            else -> 0
        }
        val y = when {
            event.deltaY > 0 -> 1
            event.deltaY < 0 -> -1
            else -> 0
        }
        newScroll = Vector2(x, y)
    }

    private fun updatePosition() {
        newPosition?.let {
            _position = it
            newPosition = null
        }
        try {
            val point = MouseInfo.getPointerInfo().location
            val local = Game.fxCanvas.screenToLocal(point.x.toDouble(), point.y.toDouble())
            _position = Vector2(local.x, local.y).clamp(Vector2.ZERO, Game.size)
        } catch (_: Exception) {
            return
        }
    }

    private fun updateDownButtons() {
        _downButtons.addAll(newPressedButtons)
        newPressedButtons.clear()
    }

    private fun updateUpButtons() {
        _upButtons.clear()
        _upButtons.addAll(MouseButton.entries)
        _upButtons.removeAll(_downButtons)
    }

    private fun updatePressedButtons() {
        _pressedButtons.clear()
        _pressedButtons.addAll(newPressedButtons)
        _pressedButtons.removeAll(_downButtons)
    }

    private fun updateReleasedButtons() {
        _releasedButtons.clear()
        _releasedButtons.addAll(newReleasedButtons)
        newReleasedButtons.clear()
        _downButtons.removeAll(_releasedButtons)
    }

    private fun updateClickedButtons() {
        _clickedButtons.clear()
        _clickedButtons.addAll(newClickedButtons)
        newClickedButtons.clear()
    }

    private fun updateScroll() {
        _scroll = newScroll
        newScroll = Vector2.ZERO
    }

    private fun move(value: Vector2) {
        if (!Game.focused) {
            return
        }
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

    private fun reset() {
        newPressedButtons.clear()
        _downButtons.clear()
        newReleasedButtons.clear()
        _releasedButtons.clear()
        newClickedButtons.clear()
        _clickedButtons.clear()
        newScroll = Vector2.ZERO
        _scroll = Vector2.ZERO
        updateUpButtons()
    }
}