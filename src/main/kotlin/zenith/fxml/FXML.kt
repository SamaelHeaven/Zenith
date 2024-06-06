package zenith.fxml

import javafx.fxml.FXMLLoader
import zenith.io.Resource
import java.io.InputStream
import java.net.URL

object FXML {
    fun <T> load(resource: String, controller: Any? = null): T {
        return load(Resource.url(resource), controller)
    }

    fun <T> load(url: URL, controller: Any? = null): T {
        url.openStream().use {
            return load(it, controller)
        }
    }

    fun <T> load(input: InputStream, controller: Any? = null): T {
        val fxmlLoader = FXMLLoader()
        fxmlLoader.setRoot(null)
        controller?.let { fxmlLoader.setController(controller) }
        return fxmlLoader.load(input)
    }
}