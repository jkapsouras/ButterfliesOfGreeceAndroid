package gr.jkapsouras.butterfliesofgreece

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider.getUriForFile
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.sansoft.butterflies.R
import com.yalantis.ucrop.UCrop
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.Permissions
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import gr.jkapsouras.butterfliesofgreece.managers.LocationManager
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    val emitter: PublishSubject<Boolean> = PublishSubject.create()
    val emitterEvents: PublishSubject<UiEvent> = PublishSubject.create()
    var imageUri: Uri? = null
    var imageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()

        StrictMode.setThreadPolicy(policy)

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
                R.id.endangeredFragment -> {
                    toolbar.context.setTheme(R.style.EndangeredTheme)
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.endangered))
                    toolbar.setTitleTextColor(applicationContext.getColor(R.color.endangered_dark))
                }
                R.id.introductionFragment -> {
                    toolbar.context.setTheme(R.style.IntroductionTheme)
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.introduction))
                    toolbar.setTitleTextColor(applicationContext.getColor(R.color.introduction_dark))
                }
                R.id.aboutFragment -> {
                    toolbar.context.setTheme(R.style.AboutTheme)
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.about))
                    toolbar.setTitleTextColor(applicationContext.getColor(R.color.about_dark))
                }
                R.id.legalFragment -> {
                    toolbar.context.setTheme(R.style.LegalTheme)
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.legal))
                    toolbar.setTitleTextColor(applicationContext.getColor(R.color.legal_dark))
                }
                R.id.recognitionFragment -> {
                    toolbar.context.setTheme(R.style.RecognitionTheme)
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.recognition))
                    toolbar.setTitleTextColor(applicationContext.getColor(R.color.recognition_dark))
                }
                R.id.searchFragment ->
                    search_bar.visibility = View.VISIBLE
                else ->
                    search_bar.visibility = View.GONE
            }
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)
        }
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onStart() {
        super.onStart()
        if(imageUri!=null)
            emitterEvents.onNext(RecognitionEvents.PhotoChosen(imageUri))
        else if(imageBitmap!=null)
            emitterEvents.onNext(RecognitionEvents.PhotoTaken(imageBitmap!!))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
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
        else if(requestCode == PERMISSION_CODE) {
            if (grantResults.isEmpty()) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(LocationManager.TAG, "User interaction was cancelled.")
                emitterEvents.onNext(RecognitionEvents.PermissionDenied)
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
//                getLastLocation()
                Log.i(LocationManager.TAG, "granted")
                emitterEvents.onNext(RecognitionEvents.PermissionGranted(Permissions.Gallery))
            }
        }
        else if(requestCode == PERMISSION_CODE_CAMERA) {
            if (grantResults.isEmpty()) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(LocationManager.TAG, "User interaction was cancelled.")
                emitterEvents.onNext(RecognitionEvents.PermissionDenied)
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
//                getLastLocation()
                Log.i(LocationManager.TAG, "granted")
                emitterEvents.onNext(RecognitionEvents.PermissionGranted(Permissions.Camera))
            }
        }
        else if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isEmpty()) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(LocationManager.TAG, "User interaction was cancelled.")
                emitterEvents.onNext(RecognitionEvents.PermissionDenied)
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
//                getLastLocation()
                Log.i(LocationManager.TAG, "granted")
                emitterEvents.onNext(RecognitionEvents.PermissionGranted(Permissions.LiveSession))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && (requestCode == IMAGE_PICK_CODE)){
            var tmpimageUri = data?.data
            cropImage(tmpimageUri!!)
            imageBitmap = null// MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)

            imageUri = null
        }
        else if(resultCode == Activity.RESULT_OK && requestCode == USE_CAMERA){
            cropImage(getCacheImagePath("temp.jpg")!!)
            imageBitmap = null

            imageUri = null
        }
        else if(resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP)
        {
            val resultUri = UCrop.getOutput(data!!)
            imageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
            imageUri = null
        }
        else if(requestCode == UCrop.REQUEST_CROP)
        {
            imageBitmap = null
            imageUri = null
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        CurrentPhotoPath = image.absolutePath
        return image
    }

    private fun cropImage(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(
            File(
                cacheDir, queryName(
                    contentResolver, sourceUri
                )
            )
        )
        val options = UCrop.Options()
        options.setCompressionQuality(IMAGE_COMPRESSION)
        options.setToolbarColor(ContextCompat.getColor(this, R.color.recognition_dark))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.recognition_dark))
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.recognition_dark))
        if (lockAspectRatio) options.withAspectRatio(
            ASPECT_RATIO_X.toFloat(),
            ASPECT_RATIO_Y.toFloat()
        )

        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight)
        UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .start(this)
    }

    fun getCacheImagePath(fileName: String): Uri? {
        val path = File(externalCacheDir, "camera")
        if (!path.exists()) path.mkdirs()
        val image = File(path, fileName)
        val x = getUriForFile(this, "$packageName.fileprovider", image)
        return x
    }

    private fun queryName(resolver: ContentResolver, uri: Uri): String? {
        val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
        val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        val name: String = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }


    companion object {

        var CurrentPhotoPath = ""
        private const val bitmapMaxWidth = 1000
        private const val bitmapMaxHeight = 1000
        private const val setBitmapMaxWidthHeight = false
        private const val lockAspectRatio = false
        private const val ASPECT_RATIO_Y = 9.0
        private const val ASPECT_RATIO_X = 16.0
        private const val IMAGE_COMPRESSION = 100
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
        private const val PERMISSION_CODE_CAMERA = 201
        private const val USE_CAMERA = 200

        const val TAG = "CameraXBasic"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}