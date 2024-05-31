package zenith.fxml

class SetWrapper<T> : HashSet<T>(), Wrapper<T> {
    override fun handle(value: T) {
        add(value)
    }
}