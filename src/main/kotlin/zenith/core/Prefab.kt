package zenith.core

import zenith.fxml.ListNode
import zenith.math.Vector2

abstract class Prefab : ListNode<Component>(), EntityProvider {
    open var name = ""
    open var position = Vector2.ZERO
    open var scale = Vector2.ZERO
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
        get() = scale.x
        set(value) {
            scale = Vector2(value, scale.y)
        }

    var height: Float
        get() = scale.y
        set(value) {
            scale = Vector2(scale.x, value)
        }

    abstract fun build(entity: Entity)

    final override fun provide(): Entity {
        val entity = Entity(name, position, scale, origin, pivotPoint, rotation, zIndex, disable, tags, components)
        entity.addComponents(this)
        build(entity)
        return entity
    }
}