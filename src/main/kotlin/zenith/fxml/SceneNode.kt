package zenith.fxml

import zenith.core.Entity
import zenith.core.EntityProvider

class SceneNode : ListNode<EntityProvider>() {
    override var content: EntityProvider?
        get() = super.content
        set(value) {
            @Suppress("UNCHECKED_CAST") if (value is Collection<*>) {
                addAll(value as Collection<EntityProvider>)
                return
            }
            super.content = value
        }

    fun loadEntities(): List<Entity> {
        val result = mutableListOf<Entity>()
        for (entityProvider in this) {
            result.add(entityProvider.provide())
        }
        return result
    }
}