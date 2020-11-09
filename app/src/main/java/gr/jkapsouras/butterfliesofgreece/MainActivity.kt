package gr.jkapsouras.butterfliesofgreece

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCenter.start(
            application, "db3cabaa-d642-449c-bb9e-35a2cb14bb35",
            Analytics::class.java, Crashes::class.java
        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController: NavController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = when (destination.id) {
                R.id.familiesFragment -> resources.getString(R.string.field_photos)
                else -> "Default title"
            }

            when (destination.id){
                R.id.contributeFragment -> {
                    toolbar.context.setTheme(R.style.ContributeTheme)
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.contribute))
                    toolbar.setTitleTextColor(applicationContext.getColor(R.color.contribute_dark))
                }
                R.id.searchFragment ->
                    search_bar.visibility = View.VISIBLE
                else ->
                    search_bar.visibility = View.GONE
            }
        }
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)


    }
}