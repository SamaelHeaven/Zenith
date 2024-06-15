package zenith.drawable

import javafx.beans.NamedArg
import javafx.scene.canvas.GraphicsContext
import zenith.core.CustomProperty
import zenith.core.Entity
import zenith.core.Property
import zenith.math.Vector2
import zenith.paint.Color
import zenith.paint.Paint

class Rectangle(
    @NamedArg("entity") entity: Entity? = null,
    @NamedArg("position") position: Vector2? = null,
    @NamedArg("offset") offset: Vector2? = null,
    @NamedArg("size") size: Vector2? = null,
    @NamedArg("origin") origin: Vector2? = null,
    @NamedArg("pivotPoint") pivotPoint: Vector2? = null,
    @NamedArg("rotation") rotation: Float? = null,
    @NamedArg("drawMode") drawMode: DrawMode? = null,
    @NamedArg("alpha") alpha: Float? = null,
    @NamedArg("fill") fill: Paint = Color.TRANSPARENT,
    @NamedArg("stroke") stroke: Paint = Color.TRANSPARENT,
    @NamedArg("strokeWidth") strokeWidth: Float = 0f,
    @NamedArg("radius") radius: Float = 0f
) : Drawable(), EntityDrawable {
    private var boundEntity: Entity? = null
    public override val positionProperty = Property(Vector2.ZERO)
    val offsetProperty = Property(Vector2.ZERO)
    val sizeProperty = Property(Vector2.ZERO)
    public override val pivotPointProperty = Property(Vector2.ZERO)
    public override val rotationProperty = Property(0f)
    val fillProperty = Property(fill)
    val strokeProperty = Property(stroke)
    val strokeWidthProperty = Property(strokeWidth)
    val radiusProperty = Property(radius)

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

    var radius: Float
        get() = radiusProperty.value
        set(value) {
            radiusProperty.value = value
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
        val topLeft = position + offset - (size * 0.5 + size * (origin * 0.5))
        val rotationPoint = (topLeft + size / 2) + pivotPoint
        return DrawMode.isVisible(
            drawMode.transform, topLeft - strokeWidth / 2, size + strokeWidth, rotationPoint, rotation
        )
    }

    override fun draw(gc: GraphicsContext) {
        val topLeft = position + offset - (size * 0.5 + size * (origin * 0.5))
        val rotationPoint = (topLeft + size / 2) + pivotPoint
        val visible = DrawMode.isVisible(
            gc.transform, topLeft - strokeWidth / 2, size + strokeWidth, rotationPoint, rotation
        )
        if (!visible) {
            return
        }
        if (rotation != 0f) {
            gc.translate(rotationPoint.x.toDouble(), rotationPoint.y.toDouble())
            gc.rotate(rotation.toDouble())
            gc.translate(-rotationPoint.x.toDouble(), -rotationPoint.y.toDouble())
        }
        if (fill != Color.TRANSPARENT) {
            drawFill(gc, topLeft)
        }
        if (stroke != Color.TRANSPARENT && strokeWidth > 0) {
            drawStroke(gc, topLeft)
        }
    }

    private fun drawFill(gc: GraphicsContext, topLeft: Vector2) {
        gc.fill = fill.fxPaint
        if (radius <= 0) {
            gc.fillRect(topLeft.x.toDouble(), topLeft.y.toDouble(), size.x.toDouble(), size.y.toDouble())
            return
        }
        gc.fillRoundRect(
            topLeft.x.toDouble(),
            topLeft.y.toDouble(),
            size.x.toDouble(),
            size.y.toDouble(),
            radius.toDouble(),
            radius.toDouble()
        )
    }

    private fun drawStroke(gc: GraphicsContext, topLeft: Vector2) {
        gc.stroke = stroke.fxPaint
        gc.lineWidth = strokeWidth.toDouble()
        if (radius <= 0) {
            gc.strokeRect(topLeft.x.toDouble(), topLeft.y.toDouble(), size.x.toDouble(), size.y.toDouble())
            return
        }
        gc.strokeRoundRect(
            topLeft.x.toDouble(),
            topLeft.y.toDouble(),
            size.x.toDouble(),
            size.y.toDouble(),
            radius.toDouble(),
            radius.toDouble()
        )
    }
}