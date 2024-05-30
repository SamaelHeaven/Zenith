package zenith.paint

class NamedColor(val color: Color, name: String) {
    companion object {
        private val colors = HashMap<String, Color>()
        fun get(name: String) = colors[name]
    }

    init {
        colors[name] = color
    }
}