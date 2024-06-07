package zenith.input

import zenith.math.Vector2
import kotlin.math.roundToInt

enum class InputAxis {
    HORIZONTAL {
        override fun get(): Int {
            return get(Key.LEFT, Key.A, Key.RIGHT, Key.D, GamepadAxis.LEFT_X)
        }
    },
    VERTICAL {
        override fun get(): Int {
            return get(Key.UP, Key.W, Key.DOWN, Key.S, GamepadAxis.LEFT_Y)
        }
    };

    abstract fun get(): Int

    companion object {
        val both: Vector2 get() = Vector2(HORIZONTAL.get(), VERTICAL.get())

        fun get(minusKey: Key, minusKeyAlt: Key, plusKey: Key, plusKeyAlt: Key, gamepadAxis: GamepadAxis): Int {
            val axis = Gamepad.first().getAxis(gamepadAxis).roundToInt()
            val minus = Keyboard.isKeyDown(minusKey) || Keyboard.isKeyDown(minusKeyAlt) || axis == -1
            val plus = Keyboard.isKeyDown(plusKey) || Keyboard.isKeyDown(plusKeyAlt) || axis == 1
            if (minus && !plus) {
                return -1
            }
            if (plus && !minus) {
                return 1
            }
            return 0
        }
    }
}