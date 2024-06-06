package zenith.input

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWGamepadState
import org.lwjgl.system.Configuration
import zenith.core.Game
import java.util.*

class Gamepad private constructor(val id: Int) {
    private val _downButtons = mutableSetOf<GamepadButton>()
    private val _upButtons = mutableSetOf<GamepadButton>()
    private val _releasedButtons = mutableSetOf<GamepadButton>()
    private val _pressedButtons = mutableSetOf<GamepadButton>()
    private val _axes = mutableMapOf<GamepadAxis, Float>()
    private val previousDownButtons = mutableSetOf<GamepadButton>()
    val name get() = GLFW.glfwGetGamepadName(id) ?: "Unknown gamepad"
    val connected get() = GLFW.glfwJoystickPresent(id) && GLFW.glfwJoystickIsGamepad(id)
    val downButtons: Set<GamepadButton> get() = Collections.unmodifiableSet(_downButtons)
    val upButtons: Set<GamepadButton> get() = Collections.unmodifiableSet(_upButtons)
    val releasedButtons: Set<GamepadButton> get() = Collections.unmodifiableSet(_releasedButtons)
    val pressedButtons: Set<GamepadButton> get() = Collections.unmodifiableSet(_pressedButtons)
    val axes: Map<GamepadAxis, Float> get() = Collections.unmodifiableMap(_axes)

    companion object {
        private val AXES = GamepadAxis.entries
        private val _gamepads = mutableListOf<Gamepad>()
        val gamepads: List<Gamepad> get() = Collections.unmodifiableList(_gamepads)

        init {
            Game.throwIfUninitialized()
            initializeGLFW()
            initializeGamepads()
        }

        internal fun update() {
            for (gamepad in _gamepads) {
                gamepad.update()
            }
        }

        private fun initializeGLFW() {
            Configuration.GLFW_CHECK_THREAD0.set(false)
            if (!GLFW.glfwInit()) {
                throw RuntimeException("Could not initialize GLFW")
            }
            Runtime.getRuntime().addShutdownHook(Thread { GLFW.glfwTerminate() })
        }

        private fun initializeGamepads() {
            for (i in GLFW.GLFW_JOYSTICK_1..GLFW.GLFW_JOYSTICK_LAST) {
                _gamepads.add(Gamepad(i))
            }
        }
    }

    init {
        for (axis in GamepadAxis.entries) {
            _axes[axis] = 0f
        }
    }

    fun isButtonDown(button: GamepadButton): Boolean {
        return _downButtons.contains(button)
    }

    fun isButtonUp(button: GamepadButton): Boolean {
        return _upButtons.contains(button)
    }

    fun isButtonPressed(button: GamepadButton): Boolean {
        return _pressedButtons.contains(button)
    }

    fun isButtonReleased(button: GamepadButton): Boolean {
        return _releasedButtons.contains(button)
    }

    fun getAxis(gamepadAxis: GamepadAxis): Float {
        return _axes[gamepadAxis]!!
    }

    private fun update() {
        if (!connected || !Game.focused) {
            reset()
            return
        }
        GLFWGamepadState.malloc().use { state ->
            GLFW.glfwGetGamepadState(id, state)
            updateDownButtons(state)
            updateAxes(state)
        }
        updateUpButtons()
        updateReleasedButtons()
        updatePressedButtons()
        updatePreviousDownButtons()
    }

    private fun updateDownButtons(state: GLFWGamepadState) {
        _downButtons.clear()
        for (i in 0 until state.buttons().limit()) {
            if (state.buttons(i).toInt() != 0) {
                GamepadButton.get(i)?.let { _downButtons.add(it) }
            }
        }
    }

    private fun updateUpButtons() {
        _upButtons.clear()
        _upButtons.addAll(GamepadButton.entries)
        _upButtons.removeAll(_downButtons)
    }

    private fun updateReleasedButtons() {
        _releasedButtons.clear()
        _releasedButtons.addAll(_downButtons)
        _releasedButtons.removeAll(previousDownButtons)
    }

    private fun updatePressedButtons() {
        _pressedButtons.clear()
        _pressedButtons.addAll(_downButtons)
        _pressedButtons.removeAll(previousDownButtons)
    }

    private fun updatePreviousDownButtons() {
        previousDownButtons.clear()
        previousDownButtons.addAll(_downButtons)
    }

    private fun updateAxes(state: GLFWGamepadState) {
        for (i in 0 until state.axes().limit()) {
            GamepadAxis.get(i)?.let { _axes[it] = state.axes(i) }
        }
    }

    private fun reset() {
        _downButtons.clear()
        _releasedButtons.clear()
        _pressedButtons.clear()
        previousDownButtons.clear()
        updateUpButtons()
        for (axis in AXES) {
            if (_axes[axis] != 0f) {
                _axes[axis] = 0f
            }
        }
    }
}