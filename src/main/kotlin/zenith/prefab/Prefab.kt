package zenith.prefab

import zenith.core.Component
import zenith.core.Entity
import zenith.core.EntityProvider
import zenith.fxml.ListNode
import zenith.fxml.StringSetNode
import zenith.math.Vector2

abstract class Prefab : ListNode<Component>(), EntityProvider {
    open var name = ""
    open var position = Vector2.ZERO
    open var scale = Vector2.ZERO
    open var origin = Vector2.ZERO
    open var pivotPoint = Vector2.ZERO
    open var rotation = 0f
    open var zIndex = 0
    open var tags: StringSetNode = StringSetNode()
    open var components: Iterable<Component> = listOf()

    open var x: Float
        get() = position.x
        set(value) {
            position = Vector2(value, position.y)
        }

    open var y: Float
        get() = position.y
        set(value) {
            position = Vector2(position.x, value)
        }

    open var width: Float
        get() = scale.x
        set(value) {
            scale = Vector2(value, scale.y)
        }

    open var height: Float
        get() = scale.y
        set(value) {
            scale = Vector2(scale.x, value)
        }

    abstract fun build(entity: Entity)

    final override fun provide(): Entity {
        val entity = Entity(name, position, scale, origin, pivotPoint, rotation, zIndex, tags, components)
        entity.addComponents(this)
        build(entity)
        return entity
    }
}