package zenith.fxml

class ListWrapper<T> : ArrayList<T>(), Wrapper<T> {
    override fun handle(value: T) {
        add(value)
    }
}