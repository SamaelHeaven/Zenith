package zenith.drawable

import zenith.core.Entity

interface EntityDrawable {
    fun bind(entity: Entity)
    fun unbind()
}