package zenith.paint

import javafx.beans.NamedArg

class NamedColor(@NamedArg("color") val color: Color, @NamedArg("name") name: String) {
    companion object {
        private val colors = mutableMapOf<String, Color>()

        @JvmStatic fun valueOf(name: String) = colors[name]
    }

    init {
        colors[name] = color
    }
}