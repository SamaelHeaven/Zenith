package zenith.input

import org.lwjgl.glfw.GLFW

enum class GamepadButton(private val code: Int) {
    A(GLFW.GLFW_GAMEPAD_BUTTON_A),
    B(GLFW.GLFW_GAMEPAD_BUTTON_B),
    X(GLFW.GLFW_GAMEPAD_BUTTON_X),
    Y(GLFW.GLFW_GAMEPAD_BUTTON_Y),
    LEFT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_BUMPER),
    RIGHT_BUMPER(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER),
    BACK(GLFW.GLFW_GAMEPAD_BUTTON_BACK),
    START(GLFW.GLFW_GAMEPAD_BUTTON_START),
    GUIDE(GLFW.GLFW_GAMEPAD_BUTTON_GUIDE),
    LEFT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_LEFT_THUMB),
    RIGHT_THUMB(GLFW.GLFW_GAMEPAD_BUTTON_RIGHT_THUMB),
    DPAD_UP(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_UP),
    DPAD_RIGHT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_RIGHT),
    DPAD_DOWN(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_DOWN),
    DPAD_LEFT(GLFW.GLFW_GAMEPAD_BUTTON_DPAD_LEFT);

    companion object {
        private val buttons: MutableMap<Int, GamepadButton> = HashMap()

        init {
            for (button in entries) {
                buttons[button.code] = button
            }
        }

        internal fun get(code: Int): GamepadButton? {
            return buttons[code]
        }
    }
}