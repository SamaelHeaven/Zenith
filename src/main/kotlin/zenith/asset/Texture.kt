package zenith.asset

import javafx.beans.NamedArg
import javafx.scene.image.Image
import zenith.core.Game
import zenith.math.Vector2
import zenith.utils.Resource
import java.net.URL

class Texture {
    internal val fxImage: Image
    val width: Float get() = fxImage.width.toFloat()
    val height: Float get() = fxImage.height.toFloat()
    val size: Vector2 get() = Vector2(width, height)

    companion object {
        private val cachedFxImages = HashMap<String, Image>()
        var cacheByDefault: Boolean = true

        @JvmStatic
        fun valueOf(value: String): Texture {
            return Texture(value)
        }
    }

    constructor(@NamedArg("url") url: URL, @NamedArg("cache") cache: Boolean = cacheByDefault) {
        val path = url.toExternalForm()
        if (cachedFxImages.containsKey(path)) {
            fxImage = cachedFxImages[path]!!
            return
        }
        fxImage = Image(path)
        if (cache) {
            cachedFxImages[path] = fxImage
        }
    }

    constructor(@NamedArg("path") path: String, @NamedArg("cache") cache: Boolean = cacheByDefault) : this(
        Resource.url(path), cache
    )

    init {
        Game.throwIfUninitialized()
    }
}