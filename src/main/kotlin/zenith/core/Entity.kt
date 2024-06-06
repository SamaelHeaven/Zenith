package zenith.core

import javafx.beans.NamedArg
import zenith.math.Vector2

class Entity : EntityProvider, Iterable<Component>, Comparable<Entity> {
    private val _components = mutableListOf<Component>()
    private val componentsToAdd = mutableListOf<Component>()
    private val componentsToRemove = mutableListOf<Component>()
    private var clean = false
    val name: String
    val positionProperty = Property(Vector2.ZERO)
    val scaleProperty = Property(Vector2.ZERO)
    val originProperty = Property(Vector2.ZERO)
    val pivotPointProperty = Property(Vector2.ZERO)
    val rotationProperty = Property(0f)
    val zIndexProperty = Property(0)
    val disableProperty = Property(false)
    val tags = mutableSetOf<String>()

    var position: Vector2
        get() = positionProperty.value
        set(value) {
            positionProperty.value = value
        }

    var scale: Vector2
        get() = scaleProperty.value
        set(value) {
            scaleProperty.value = value
        }

    var origin: Vector2
        get() = originProperty.value
        set(value) {
            originProperty.value = value
        }

    var pivotPoint: Vector2
        get() = pivotPointProperty.value
        set(value) {
            pivotPointProperty.value = value
        }

    var rotation: Float
        get() = rotationProperty.value
        set(value) {
            rotationProperty.value = value
        }

    var zIndex: Int
        get() = zIndexProperty.value
        set(value) {
            zIndexProperty.value = value
        }

    var disable: Boolean
        get() = disableProperty.value
        set(value) {
            disableProperty.value = value
        }

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

    constructor(
        @NamedArg("name") name: String = "",
        @NamedArg("position") position: Vector2 = Vector2.ZERO,
        @NamedArg("scale") scale: Vector2 = Vector2.ZERO,
        @NamedArg("origin") origin: Vector2 = Vector2.ZERO,
        @NamedArg("pivotPoint") pivotPoint: Vector2 = Vector2.ZERO,
        @NamedArg("rotation") rotation: Float = 0f,
        @NamedArg("zIndex") zIndex: Int = 0,
        @NamedArg("disable") disable: Boolean = false,
        @NamedArg("tags") tags: Set<String> = setOf(),
        @NamedArg("components") components: Iterable<Component> = listOf()
    ) {
        this.name = name
        this.tags.addAll(tags)
        this.position = position
        this.scale = scale
        this.origin = origin
        this.pivotPoint = pivotPoint
        this.rotation = rotation
        this.zIndex = zIndex
        this.disable = disable
        addComponents(components)
    }

    override fun provide(): Entity {
        return this
    }

    override fun compareTo(other: Entity): Int {
        return zIndex.compareTo(other.zIndex)
    }

    override fun iterator(): Iterator<Component> {
        return _components.iterator()
    }

    inline fun <reified T> getComponent(): T? {
        return getComponent(T::class.java)
    }

    fun <T> getComponent(clazz: Class<T>): T? {
        for (component in _components) {
            if (clazz.isAssignableFrom(component.javaClass)) {
                return clazz.cast(component)
            }
        }
        return null
    }

    inline fun <reified T> getComponents(): List<T> {
        return getComponents(T::class.java)
    }

    fun <T> getComponents(clazz: Class<T>): List<T> {
        val result = mutableListOf<T>()
        for (component in _components) {
            if (clazz.isAssignableFrom(component.javaClass)) {
                result.add(clazz.cast(component))
            }
        }
        return result
    }

    fun addComponent(component: Component) {
        if (_components.contains(component)) {
            return
        }
        if (component.entity != null) {
            throw UnsupportedOperationException("A component cannot be attached to multiple entities")
        }
        componentsToAdd.add(component)
        clean = false
        if (Game.scene.initialized) {
            return
        }
        clean()
    }

    fun addComponents(vararg components: Component) {
        addComponents(components.toList())
    }

    fun <T : Component> addComponents(components: Iterable<T>) {
        for (component in components) {
            addComponent(component)
        }
    }

    fun removeComponent(component: Component) {
        componentsToRemove.add(component)
        clean = false
        if (Game.scene.initialized) {
            return
        }
        clean()
    }

    fun <T : Component> removeComponents(components: Iterable<T>) {
        for (component in components) {
            removeComponent(component)
        }
    }

    inline fun <reified T> removeComponents() {
        removeComponents(T::class.java)
    }

    fun <T> removeComponents(clazz: Class<T>) {
        for (component in _components) {
            if (clazz.isAssignableFrom(component.javaClass)) {
                componentsToRemove.add(component)
            }
        }
        clean = false
        if (Game.scene.initialized) {
            return
        }
        clean()
    }

    internal fun start() {
        for (component in getSafeComponents()) {
            component.callStart()
        }
        clean()
    }

    internal fun update() {
        if (disable) {
            return
        }
        for (component in _components) {
            component.callUpdate()
        }
        clean()
    }

    internal fun fixedUpdate() {
        if (disable) {
            return
        }
        for (component in _components) {
            component.callFixedUpdate()
        }
        clean()
    }

    internal fun destroy() {
        for (component in getSafeComponents()) {
            component.callDestroy()
        }
        clean()
    }

    private fun getSafeComponents(): List<Component> {
        if (Game.scene.initialized) {
            return _components
        }
        return _components.toList()
    }

    private fun clean() {
        if (clean) {
            return
        }
        do {
            clean = true
            addPendingComponents()
            removePendingComponents()
        } while (!clean)
    }

    private fun addPendingComponents() {
        val componentsToAdd = componentsToAdd.toSet()
        _components.addAll(componentsToAdd)
        for (component in componentsToAdd) {
            component.initialize(this)
            if (Game.scene.contains(this)) {
                component.callStart()
            }
        }
        this.componentsToAdd.removeAll(componentsToAdd)
    }

    private fun removePendingComponents() {
        val componentsToRemove = componentsToRemove.toSet()
        _components.removeAll(componentsToRemove)
        for (component in componentsToRemove) {
            component.callDestroy()
            component.initialize(null)
        }
        this.componentsToRemove.removeAll(componentsToRemove)
    }
}