package gr.jkapsouras.butterfliesofgreece

import android.content.pm.PackageManager
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
import gr.jkapsouras.butterfliesofgreece.managers.LocationManager
import gr.jkapsouras.butterfliesofgreece.managers.LocationState
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {
    val emitter: PublishSubject<Boolean> = PublishSubject.create()

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                   grantResults: IntArray) {
        Log.i(LocationManager.TAG, "onRequestPermissionResult")
        if (requestCode == LocationManager.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isEmpty()) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(LocationManager.TAG, "User interaction was cancelled.")
                emitter.onNext(false)
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
//                getLastLocation()
                Log.i(LocationManager.TAG, "granted")
                emitter.onNext(true)
            } else {
                emitter.onNext(false)
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
//                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
//                    View.OnClickListener {
//                        // Build intent that displays the App settings screen.
//                        val intent = Intent()
//                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                        val uri = Uri.fromParts("package",
//                            BuildConfig.APPLICATION_ID, null)
//                        intent.data = uri
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        startActivity(intent)
//                    })
            }
        }
    }
}