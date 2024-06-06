package zenith.core

import zenith.math.Vector2

abstract class Prefab : EntityProvider {
    open var name = ""
    open var position = Vector2.ZERO
    open var size = Vector2.ZERO
    open var origin = Vector2.ZERO
    open var pivotPoint = Vector2.ZERO
    open var rotation = 0f
    open var zIndex = 0
    open var disable = false
    open var tags: Set<String> = setOf()
    open var components: Iterable<Component> = listOf()

    var x: Float
        get() = position.x
        set(value) {
            position = Vector2(value, position.y)
        }

    var y: Float
        get() = position.y
        set(value) {
            position = Vector2(position.x, value)
        }

    var width: Float
        get() = size.x
        set(value) {
            size = Vector2(value, size.y)
        }

    var height: Float
        get() = size.y
        set(value) {
            size = Vector2(size.x, value)
        }

    abstract fun build(entity: Entity)

    final override fun provide(): Entity {
        val entity = Entity(name, position, size, origin, pivotPoint, rotation, zIndex, disable, tags, components)
        build(entity)
        return entity
    }
}