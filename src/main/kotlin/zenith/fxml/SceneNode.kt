package zenith.fxml

import zenith.core.Entity
import zenith.core.EntityProvider

class SceneNode: ListNode<EntityProvider>() {
    fun loadEntities(): List<Entity> {
        val result = mutableListOf<Entity>()
        for (entityProvider in this) {
            result.add(entityProvider.provide())
        }
        return result
    }
}