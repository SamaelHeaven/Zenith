package zenith.fxml

import javafx.fxml.FXMLLoader
import zenith.io.Resource
import java.io.InputStream
import java.net.URL

object FXML {
    fun <T> load(resource: String): T {
        return load(Resource.url(resource))
    }

    fun <T> load(url: URL): T {
        val fxmlLoader = FXMLLoader(url)
        return fxmlLoader.load()
    }

    fun <T> load(input: InputStream): T {
        val fxmlLoader = FXMLLoader()
        return fxmlLoader.load(input)
    }
}