package zenith.drawable

import javafx.beans.NamedArg
import javafx.scene.canvas.GraphicsContext
import zenith.asset.Texture
import zenith.core.Entity
import zenith.core.Property
import zenith.math.Vector2

class Sprite(
    @NamedArg("entity") entity: Entity? = null,
    @NamedArg("position") position: Vector2? = null,
    @NamedArg("offset") offset: Vector2? = null,
    @NamedArg("size") size: Vector2? = null,
    @NamedArg("origin") origin: Vector2? = null,
    @NamedArg("pivotPoint") pivotPoint: Vector2? = null,
    @NamedArg("rotation") rotation: Float? = null,
    @NamedArg("drawMode") drawMode: DrawMode? = null,
    @NamedArg("alpha") alpha: Float? = null,
    @NamedArg("texture") texture: Texture? = null,
    @NamedArg("flippedHorizontally") flippedHorizontally: Boolean = false,
    @NamedArg("flippedVertically") flippedVertically: Boolean = false
) : Drawable(), EntityDrawable {
    private var boundEntity: Entity? = null
    public override val positionProperty = Property(Vector2.ZERO)
    val offsetProperty = Property(Vector2.ZERO)
    val sizeProperty = Property(Vector2.ZERO)
    public override val originProperty = object : Property<Vector2>(Vector2.ZERO) {
        override fun set(value: Vector2) {
            super.set(value.clamp(-1, 1))
        }
    }
    public override val pivotPointProperty = Property(Vector2.ZERO)
    public override val rotationProperty = Property(0f)
    val textureProperty: Property<Texture?> = Property(texture)
    val flippedHorizontallyProperty = Property(flippedHorizontally)
    val flippedVerticallyProperty = Property(flippedVertically)

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

    var size: Vector2
        get() = sizeProperty.value
        set(value) {
            sizeProperty.value = value
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

    var texture: Texture?
        get() = textureProperty.value
        set(value) {
            textureProperty.value = value
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

    init {
        entity?.let { bind(entity) }
        position?.let { this.position = it }
        offset?.let { this.offset = it }
        size?.let { this.size = it }
        origin?.let { this.origin = it }
        pivotPoint?.let { this.pivotPoint = it }
        rotation?.let { this.rotation = it }
        drawMode?.let { this.drawMode = it }
        alpha?.let { this.alpha = it }
        if (this.size == Vector2.ZERO) {
            this.size = texture?.size ?: Vector2.ZERO
        }
    }

    override fun bind(entity: Entity) {
        unbind()
        boundEntity = entity
        positionProperty.bindBidirectional(entity.positionProperty)
        sizeProperty.bindBidirectional(entity.scaleProperty)
        originProperty.bindBidirectional(entity.originProperty)
        pivotPointProperty.bindBidirectional(entity.pivotPointProperty)
        rotationProperty.bindBidirectional(entity.rotationProperty)
    }

    override fun unbind() {
        boundEntity?.let {
            positionProperty.unbindBidirectional(it.positionProperty)
            sizeProperty.unbindBidirectional(it.scaleProperty)
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
        if (texture == null) {
            return
        }
        val topLeft = position + offset - (size * 0.5 + size * (origin * 0.5))
        val rotationPoint = (topLeft + size / 2) + pivotPoint
        val visible = DrawMode.isVisible(
            gc.transform, topLeft, size, rotationPoint, rotation
        )
        if (!visible) {
            return
        }
        if (rotation != 0f) {
            gc.translate(rotationPoint.x.toDouble(), rotationPoint.y.toDouble())
            gc.rotate(rotation.toDouble())
            gc.translate(-rotationPoint.x.toDouble(), -rotationPoint.y.toDouble())
        }
        var actualPosition = topLeft
        var actualSize = size
        if (flippedHorizontally) {
            actualPosition += Vector2.RIGHT * size
            actualSize = Vector2(-actualSize.x, actualSize.y)
        }
        if (flippedVertically) {
            actualPosition += Vector2.DOWN * size
            actualSize = Vector2(actualSize.x, -actualSize.y)
        }
        gc.drawImage(
            texture?.fxImage,
            actualPosition.x.toDouble(),
            actualPosition.y.toDouble(),
            actualSize.x.toDouble(),
            actualSize.y.toDouble()
        )
    }
}