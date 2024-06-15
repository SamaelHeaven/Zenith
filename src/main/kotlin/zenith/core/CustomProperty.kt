package zenith.core

class CustomProperty<T>(
    initialValue: T, private val handler: (
        property: Property<T>, value: T, setter: ((value: T) -> Unit)
    ) -> Unit
) : Property<T>(initialValue) {
    private val setter = { value: T -> super.set(value) }

    override fun set(value: T) {
        handler(this, value, setter)
    }
}