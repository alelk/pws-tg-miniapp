import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.kirillNay.telegram.miniapp.compose.telegramWebApp
import com.kirillNay.telegram.miniapp.webApp.webApp
import io.github.alelk.pws.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    telegramWebApp { style ->
        val userName = webApp.initDataUnsafe.user?.firstName + " " + webApp.initDataUnsafe.user?.lastName
        ComposeViewport {
            App(userName)
        }
    }
}