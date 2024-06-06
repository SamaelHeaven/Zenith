package zenith.core

import java.util.*

abstract class Scene : Iterable<Entity> {
    private var _initialized = false
    private var clean = false
    private val _entities = mutableListOf<Entity>()
    private val entitiesToAdd = mutableListOf<Entity>()
    private val entitiesToRemove = mutableListOf<Entity>()
    internal val initialized get() = _initialized
    val entities: List<Entity> get() = Collections.unmodifiableList(_entities)
    val camera = Camera()

    protected abstract fun initialize()

    protected abstract fun update()

    fun findEntityByName(name: String): Entity? {
        for (entity in _entities) {
            if (entity.name == name) {
                return entity
            }
        }
        return null
    }

    fun findEntitiesByName(name: String): List<Entity> {
        val result = mutableListOf<Entity>()
        for (entity in _entities) {
            if (entity.name == name) {
                result.add(entity)
            }
        }
        return result
    }

    inline fun <reified T> findEntityByType(): T? {
        return findEntityByType(T::class.java)
    }

    fun <T> findEntityByType(clazz: Class<T>): T? {
        for (entity in _entities) {
            val component = entity.getComponent(clazz)
            if (component != null) {
                return component
            }
        }
        return null
    }

    inline fun <reified T> findEntitiesByType(): List<T> {
        return findEntitiesByType(T::class.java)
    }

    fun <T> findEntitiesByType(clazz: Class<T>): List<T> {
        val result = mutableListOf<T>()
        for (entity in _entities) {
            val component = entity.getComponent(clazz)
            if (component != null) {
                result.add(component)
            }
        }
        return result
    }

    fun findEntityByTag(tag: String): Entity? {
        for (entity in _entities) {
            if (entity.tags.contains(tag)) {
                return entity
            }
        }
        return null
    }

    fun findEntitiesByTag(tag: String): List<Entity> {
        val result = mutableListOf<Entity>()
        for (entity in _entities) {
            if (entity.tags.contains(tag)) {
                result.add(entity)
            }
        }
        return result
    }

    fun addEntity(entity: Entity) {
        if (_entities.contains(entity)) {
            return
        }
        if (!initialized) {
            _entities.add(entity)
            entity.start()
            return
        }
        entitiesToAdd.add(entity)
        clean = false
    }

    fun addEntities(entities: Iterable<Entity>) {
        if (initialized) {
            entitiesToAdd.addAll(entities)
            clean = false
            return
        }
        _entities.addAll(entities)
        for (entity in _entities) {
            entity.start()
        }
    }

    fun removeEntity(entity: Entity) {
        if (!initialized) {
            _entities.remove(entity)
            entity.destroy()
            return
        }
        entitiesToRemove.add(entity)
        clean = false
    }

    inline fun <reified T> removeEntities() {
        removeEntities(T::class.java)
    }

    fun <T> removeEntities(clazz: Class<T>) {
        for (entity in _entities) {
            if (clazz.isAssignableFrom(entity.javaClass)) {
                removeEntity(entity)
            }
        }
    }

    fun removeEntities(entities: Iterable<Entity>) {
        for (entity in entities) {
            removeEntity(entity)
        }
    }

    final override fun iterator(): Iterator<Entity> {
        return _entities.iterator()
    }

    internal fun callInitialize() {
        if (!_initialized) {
            initialize()
            Time.restart()
            _initialized = true
        }
    }

    internal fun callUpdate() {
        update()
        clean()
        _entities.sort()
        for (entity in _entities) {
            entity.update()
        }
        // TODO: Update physics
    }

    private fun clean() {
        if (clean) {
            return
        }
        do {
            clean = true
            addPendingEntities()
            removePendingEntities()
        } while (!clean)
    }

    private fun addPendingEntities() {
        val entitiesToAdd = entitiesToAdd.toSet()
        _entities.addAll(entitiesToAdd)
        for (entity in entitiesToAdd) {
            entity.start()
        }
        this.entitiesToAdd.removeAll(entitiesToAdd)
    }

    private fun removePendingEntities() {
        val entitiesToRemove = entitiesToRemove.toSet()
        _entities.removeAll(entitiesToRemove)
        for (entity in entitiesToRemove) {
            entity.destroy()
        }
        this.entitiesToRemove.removeAll(entitiesToRemove)
    }
}