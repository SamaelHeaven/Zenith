package zenith.input

import org.lwjgl.glfw.GLFW

enum class GamepadAxis(private val code: Int) {
    LEFT_X(GLFW.GLFW_GAMEPAD_AXIS_LEFT_X),
    LEFT_Y(GLFW.GLFW_GAMEPAD_AXIS_LEFT_Y),
    RIGHT_X(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_X),
    RIGHT_Y(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_Y),
    LEFT_TRIGGER(GLFW.GLFW_GAMEPAD_AXIS_LEFT_TRIGGER),
    RIGHT_TRIGGER(GLFW.GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER);

    companion object {
        private val axes: MutableMap<Int, GamepadAxis> = HashMap()

        init {
            for (axis in entries) {
                axes[axis.code] = axis
            }
        }

        internal fun get(code: Int): GamepadAxis? {
            return axes[code]
        }
    }
}