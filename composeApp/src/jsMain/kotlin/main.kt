import com.kirillNay.telegram.miniapp.compose.telegramWebApp
import com.kirillNay.telegram.miniapp.webApp.webApp
import io.github.alelk.pws.App

fun main() {
    telegramWebApp { style ->
        val userName = webApp.initDataUnsafe.user?.firstName + " " + webApp.initDataUnsafe.user?.lastName
        App(userName)
    }
}