package zenith.drawable

import javafx.beans.NamedArg
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Text
import zenith.asset.Font
import zenith.asset.Texture
import zenith.core.*
import zenith.math.BoundingBox
import zenith.math.Vector2
import zenith.paint.Color
import zenith.paint.Paint

class Text(
    @NamedArg("text") text: String,
    @NamedArg("entity") entity: Entity? = null,
    @NamedArg("position") position: Vector2? = null,
    @NamedArg("offset") offset: Vector2? = null,
    @NamedArg("origin") origin: Vector2? = null,
    @NamedArg("pivotPoint") pivotPoint: Vector2? = null,
    @NamedArg("rotation") rotation: Float? = null,
    @NamedArg("drawMode") drawMode: DrawMode? = null,
    @NamedArg("alpha") alpha: Float? = null,
    @NamedArg("flippedHorizontally") flippedHorizontally: Boolean = false,
    @NamedArg("flippedVertically") flippedVertically: Boolean = false,
    @NamedArg("smooth") smooth: Boolean = false,
    @NamedArg("font") font: Font = Font.default,
    @NamedArg("fill") fill: Paint = Color.TRANSPARENT,
    @NamedArg("stroke") stroke: Paint = Color.TRANSPARENT,
    @NamedArg("strokeWidth") strokeWidth: Float = 0f
) : Drawable(), EntityDrawable {
    private var texture: Texture? = null
    private val _sizeProperty = Property(Vector2.ZERO)
    private var boundEntity: Entity? = null
    public override val positionProperty = Property(Vector2.ZERO)
    val offsetProperty = Property(Vector2.ZERO)
    val sizeProperty = ReadOnlyProperty(_sizeProperty)
    public override val pivotPointProperty = Property(Vector2.ZERO)
    public override val rotationProperty = Property(0f)
    val flippedHorizontallyProperty = Property(flippedHorizontally)
    val flippedVerticallyProperty = Property(flippedVertically)
    val smoothProperty = Property(smooth)
    val textProperty = customProperty(text)
    val fontProperty = customProperty(font)
    val fillProperty = customProperty(fill)
    val strokeProperty = customProperty(stroke)
    val strokeWidthProperty = customProperty(strokeWidth)
    val size: Vector2 get() = sizeProperty.value

    public override val originProperty: Property<Vector2> = CustomProperty(Vector2.ZERO) { _, value, setter ->
        setter(value.clamp(-1, 1))
    }

    public override var position: Vector2
        get() = positionProperty.value
        set(value) {
            positionProperty.value = value
        }

    var offset: Vector2
        get() = offsetProperty.value
        set(value) {
            offsetProperty.value = value
        }

    public override var origin: Vector2
        get() = originProperty.value
        set(value) {
            originProperty.value = value
        }

    public override var pivotPoint: Vector2
        get() = pivotPointProperty.value
        set(value) {
            pivotPointProperty.value = value
        }

    public override var rotation: Float
        get() = rotationProperty.value
        set(value) {
            rotationProperty.value = value
        }

    var flippedHorizontally: Boolean
        get() = flippedHorizontallyProperty.value
        set(value) {
            flippedHorizontallyProperty.value = value
        }

    var flippedVertically: Boolean
        get() = flippedVerticallyProperty.value
        set(value) {
            flippedVerticallyProperty.value = value
        }

    var smooth: Boolean
        get() = smoothProperty.value
        set(value) {
            smoothProperty.value = value
        }

    var text: String
        get() = textProperty.value
        set(value) {
            textProperty.value = value
        }

    var font: Font
        get() = fontProperty.value
        set(value) {
            fontProperty.value = value
        }

    var fill: Paint
        get() = fillProperty.value
        set(value) {
            fillProperty.value = value
        }

    var stroke: Paint
        get() = strokeProperty.value
        set(value) {
            strokeProperty.value = value
        }

    var strokeWidth: Float
        get() = strokeWidthProperty.value
        set(value) {
            strokeWidthProperty.value = value
        }

    public override var boundingBox: BoundingBox
        get() = BoundingBox.from(position, size, origin, pivotPoint, rotation)
        set(_) {
            throw UnsupportedOperationException("Text bounding box cannot be changed")
        }

    init {
        entity?.let { bind(entity) }
        position?.let { this.position = it }
        offset?.let { this.offset = it }
        origin?.let { this.origin = it }
        pivotPoint?.let { this.pivotPoint = it }
        rotation?.let { this.rotation = it }
        drawMode?.let { this.drawMode = it }
        alpha?.let { this.alpha = it }
        updateTexture()
    }

    override fun bind(entity: Entity) {
        unbind()
        boundEntity = entity
        positionProperty.bindBidirectional(entity.positionProperty)
        originProperty.bindBidirectional(entity.originProperty)
        pivotPointProperty.bindBidirectional(entity.pivotPointProperty)
        rotationProperty.bindBidirectional(entity.rotationProperty)
    }

    override fun unbind() {
        boundEntity?.let {
            positionProperty.unbindBidirectional(it.positionProperty)
            originProperty.unbindBidirectional(it.originProperty)
            pivotPointProperty.unbindBidirectional(it.pivotPointProperty)
            rotationProperty.unbindBidirectional(it.rotationProperty)
            boundEntity = null
        }
    }

    override fun isVisible(): Boolean {
        return drawMode.isVisible(position, size, origin, pivotPoint, rotation)
    }

    override fun draw(gc: GraphicsContext) {

    }

    private fun <T> customProperty(initialValue: T): CustomProperty<T> {
        return CustomProperty(initialValue) { property, value, setter ->
            if (value == property.value) {
                return@CustomProperty
            }
            setter(value)
            updateTexture()
        }
    }

    private fun updateTexture() {
        val canvas = Renderer.offscreenCanvas
        val textNode = Text(text)
        textNode.font = font.fxFont
        val bounds = textNode.layoutBounds
        _sizeProperty.value = Vector2(bounds.width, bounds.height)
        canvas.width = size.x.toDouble()
        canvas.height = size.y.toDouble()

    }
}