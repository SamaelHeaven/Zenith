package zenith.drawable

import javafx.beans.NamedArg
import javafx.scene.canvas.GraphicsContext
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
    @NamedArg("renderingMode") drawMode: DrawMode? = null,
    @NamedArg("fill") fill: Paint = Color.TRANSPARENT,
    @NamedArg("stroke") stroke: Paint = Color.TRANSPARENT,
    @NamedArg("strokeWidth") strokeWidth: Float = 0f
) : Drawable() {
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
    val fillProperty: Property<Paint>
    val strokeProperty: Property<Paint>
    val strokeWidthProperty: Property<Float>

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

    init {
        entity?.let { bind(entity) }
        position?.let { this.position = it }
        offset?.let { this.offset = it }
        size?.let { this.size = it }
        origin?.let { this.origin = it }
        pivotPoint?.let { this.pivotPoint = it }
        rotation?.let { this.rotation = it }
        drawMode?.let { this.drawMode = it }
        fillProperty = Property(fill)
        strokeProperty = Property(stroke)
        strokeWidthProperty = Property(strokeWidth)
    }

    fun bind(entity: Entity) {
        unbind()
        boundEntity = entity
        positionProperty.bindBidirectional(entity.positionProperty)
        sizeProperty.bindBidirectional(entity.scaleProperty)
        originProperty.bindBidirectional(entity.originProperty)
        pivotPointProperty.bindBidirectional(entity.pivotPointProperty)
        rotationProperty.bindBidirectional(entity.rotationProperty)
    }

    fun unbind() {
        boundEntity?.let {
            positionProperty.unbindBidirectional(it.positionProperty)
            sizeProperty.unbindBidirectional(it.scaleProperty)
            originProperty.unbindBidirectional(it.originProperty)
            pivotPointProperty.unbindBidirectional(it.pivotPointProperty)
            rotationProperty.unbindBidirectional(it.rotationProperty)
            boundEntity = null
        }
    }

    fun isVisible(): Boolean {
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
            gc.fill = fill.fxPaint
            gc.fillRect(topLeft.x.toDouble(), topLeft.y.toDouble(), size.x.toDouble(), size.y.toDouble())
        }
        if (stroke != Color.TRANSPARENT && strokeWidth > 0) {
            gc.stroke = stroke.fxPaint
            gc.lineWidth = strokeWidth.toDouble()
            gc.strokeRect(topLeft.x.toDouble(), topLeft.y.toDouble(), size.x.toDouble(), size.y.toDouble())
        }
    }
}