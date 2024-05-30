package zenith.paint

class NamedColor(val color: Color, name: String) {
    companion object {
        private val colors: MutableMap<String, Color> = HashMap()
        fun get(name: String): Color? = colors[name]
    }

    init {
        colors[name] = color
    }
}