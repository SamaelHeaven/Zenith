package zenith.paint

import javafx.scene.paint.Paint

abstract class Paint {
    internal abstract val fxPaint: Paint

    companion object {
        @JvmStatic
        fun valueOf(name: String): zenith.paint.Paint = Color.valueOf(name)
    }
}