package movie.metropolis.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import movie.metropolis.app.screen.Route

class ShortcutActivity : Activity() {

    private val route
        get() = when (intent.action) {
            "movie.metropolis.app.ACTION_TICKETS" -> Route.Home.deepLink(Route.Tickets())
            else -> Route.Home.deepLink()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
            .setAction(Intent.ACTION_VIEW)
            .setData(route)
            .setPackage(packageName)

        window.setWindowAnimations(0)
        startActivity(intent)
        finish()
    }

}