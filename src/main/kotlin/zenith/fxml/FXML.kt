package zenith.fxml

import javafx.fxml.FXMLLoader
import zenith.io.Resource
import java.io.InputStream
import java.net.URL

object FXML {
    private val fxmlLoader = FXMLLoader()

    fun <T> load(resource: String): T {
        return load(Resource.url(resource))
    }

    fun <T> load(url: URL): T {
        url.openStream().use {
            return load(it)
        }
    }

    fun <T> load(input: InputStream): T {
        return fxmlLoader.load(input)
    }
}