package zenith.core

import zenith.math.Vector2

abstract class Component {
    private var _entity: Entity? = null
    protected open val positionProperty get() = entity!!.positionProperty
    protected open val scaleProperty get() = entity!!.scaleProperty
    protected open val originProperty get() = entity!!.originProperty
    protected open val pivotPointProperty get() = entity!!.pivotPointProperty
    protected open val rotationProperty get() = entity!!.rotationProperty
    val entity: Entity? get() = _entity

    protected open var position: Vector2
        get() = entity!!.position
        set(value) {
            entity!!.position = value
        }

    protected open var scale: Vector2
        get() = entity!!.scale
        set(value) {
            entity!!.scale = value
        }

    protected open var origin: Vector2
        get() = entity!!.origin
        set(value) {
            entity!!.origin = value
        }

    protected open var pivotPoint: Vector2
        get() = entity!!.pivotPoint
        set(value) {
            entity!!.pivotPoint = value
        }

    protected open var rotation: Float
        get() = entity!!.rotation
        set(value) {
            entity!!.rotation = value
        }

    protected open var boundingBox
        get() = entity!!.boundingBox
        set(value) {
            entity!!.boundingBox = value
        }

    protected open fun start() {}

    protected open fun update() {}

    protected open fun fixedUpdate() {}

    protected open fun destroy() {}

    protected inline fun <reified T> getComponent(): T? {
        return entity!!.getComponent()
    }

    protected fun <T> getComponent(clazz: Class<T>): T? {
        return entity!!.getComponent(clazz)
    }

    protected inline fun <reified T> getComponents(): List<T> {
        return entity!!.getComponents()
    }

    protected fun <T> getComponents(clazz: Class<T>): List<T> {
        return entity!!.getComponents(clazz)
    }

    protected fun addComponent(component: Component) {
        entity!!.addComponent(component)
    }

    protected fun addComponents(vararg components: Component) {
        entity!!.addComponents(components.toList())
    }

    protected fun <T : Component> addComponents(components: Iterable<T>) {
        entity!!.addComponents(components)
    }

    protected fun removeComponent(component: Component) {
        entity!!.removeComponent(component)
    }

    protected fun <T : Component> removeComponents(components: Iterable<T>) {
        entity!!.removeComponents(components)
    }

    protected inline fun <reified T> removeComponents() {
        entity!!.removeComponents<T>()
    }

    protected fun <T> removeComponents(clazz: Class<T>) {
        entity!!.removeComponents(clazz)
    }

    internal fun callStart() {
        start()
    }

    internal fun callUpdate() {
        update()
    }

    internal fun callFixedUpdate() {
        fixedUpdate()
    }

    internal fun callDestroy() {
        destroy()
    }

    internal fun initialize(entity: Entity?) {
        this._entity = entity
    }
}