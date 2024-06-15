package zenith.fxml

import javafx.fxml.FXMLLoader
import zenith.io.Resource
import java.io.InputStream
import java.net.URL

object FXML {
    private val fxmlLoader = FXMLLoader()

    fun <T> load(resource: String, controller: Any? = null): T {
        return load(Resource.url(resource), controller)
    }

    fun <T> load(url: URL, controller: Any? = null): T {
        url.openStream().use {
            return load(it, controller, url)
        }
    }

    fun <T> load(input: InputStream, controller: Any? = null, location: URL? = null): T {
        fxmlLoader.setRoot(null)
        fxmlLoader.location = location
        fxmlLoader.setController(controller)
        return fxmlLoader.load(input)
    }
}