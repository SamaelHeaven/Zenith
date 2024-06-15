package zenith.asset

import javafx.beans.NamedArg
import javafx.embed.swing.SwingFXUtils
import javafx.scene.image.Image
import zenith.core.Game
import zenith.io.Path
import zenith.io.Resource
import zenith.math.BoundingBox
import zenith.math.Vector2
import java.io.File
import java.io.OutputStream
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

class Texture {
    internal val fxImage: Image
    val width get() = fxImage.width.toFloat()
    val height get() = fxImage.height.toFloat()
    val size get() = Vector2(width, height)
    val boundingBox get() = BoundingBox(0f, 0f, width, height)

    companion object {
        private val cachedFxImages = mutableMapOf<String, Image>()
        var cacheByDefault = true

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

    constructor(@NamedArg("resource") resource: String, @NamedArg("cache") cache: Boolean = cacheByDefault) : this(
        Resource.url(resource), cache
    )

    internal constructor(fxImage: Image) {
        this.fxImage = fxImage
    }

    init {
        Game.throwIfUninitialized()
    }

    fun save(path: String, format: Format = Format.PNG) {
        val file = File(Path.format(path))
        file.outputStream().use { save(it, format) }
    }

    fun save(output: OutputStream, format: Format = Format.PNG) {
        ImageIO.write(SwingFXUtils.fromFXImage(fxImage, null), format.name.lowercase(Locale.ENGLISH), output)
    }

    enum class Format { GIF, PNG, JPG }
}