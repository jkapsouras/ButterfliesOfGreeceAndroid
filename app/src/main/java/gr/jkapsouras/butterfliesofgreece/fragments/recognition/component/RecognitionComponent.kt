package gr.jkapsouras.butterfliesofgreece.fragments.recognition.component

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.graphics.toRect
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.viewStates.RecognitionViewStates
import gr.jkapsouras.butterfliesofgreece.managers.detection.Detector
import gr.jkapsouras.butterfliesofgreece.views.cameraView.CameraView
import gr.jkapsouras.butterfliesofgreece.views.cameraView.MultiBoxTracker
import gr.jkapsouras.butterfliesofgreece.views.cameraView.OverlayView
import gr.jkapsouras.butterfliesofgreece.views.recognitionView.RecognitionView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream


class RecognitionComponent(
    private val activity: MainActivity,
    private val chooseButton: Button,
    takePhotoButton: Button,
    liveSessionButton: Button,
    private val recognitionView: RecognitionView,
    private val cameraView: CameraView,
    private val overlayView: OverlayView
) : UiComponent {

    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {

       chooseButton.setOnClickListener {
            event.onNext(RecognitionEvents.ChoosePhotoClicked)
            Log.d(Constraints.TAG, "choose photo clicked")
        }

        takePhotoButton.setOnClickListener {
            event.onNext(RecognitionEvents.TakePhotoClicked)
            Log.d(Constraints.TAG, "take photo clicked")
        }

        liveSessionButton.setOnClickListener {
            event.onNext(RecognitionEvents.LiveRecognitionClicked)
            Log.d(Constraints.TAG, "live clicked")
        }

        uiEvents = Observable.merge(
            recognitionView.uiEvents,
            event,
            activity.emitterEvents,
            cameraView.uiEvents
        )

        recognitionView.visibility = View.GONE

        cameraView.activity = activity
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(activity, intent, 1000, null)
    }

    private fun pickImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, activity.getCacheImagePath("temp.jpg"));
        if (cameraIntent.resolveActivity(activity.packageManager) != null) {
            startActivityForResult(activity, cameraIntent, 200, null)
        }
    }

    override fun renderViewState(viewState: ViewState) {
        if (viewState is RecognitionViewStates) {
            when (viewState) {
                is RecognitionViewStates.ShowGallery -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED
                        ) {
                            //permission denied
                            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                            //show popup to request runtime permission
                            activity.requestPermissions(permissions, 1001);
                        } else {
                            //permission already granted
                            pickImageFromGallery();
                        }
                    } else {
                        //system OS is < Marshmallow
                        pickImageFromGallery();
                    }
                }
                is RecognitionViewStates.ShowCamera -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED
                        ) {
                            //permission denied
                            val permissions = arrayOf(Manifest.permission.CAMERA);
                            //show popup to request runtime permission
                            activity.requestPermissions(permissions, 201);
                        } else {
                            //permission already granted
                            pickImageFromCamera();
                        }
                    } else {
                        //system OS is < Marshmallow
                        pickImageFromCamera();
                    }
                }
                is RecognitionViewStates.ShowPermissionDenied -> {
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                is RecognitionViewStates.ShowRecognitionView -> {
                    recognitionView.visibility = View.VISIBLE
                    chooseButton.visibility = View.INVISIBLE
                    recognitionView.showSelectedImage(image = viewState.image)
                }
                is RecognitionViewStates.ShowRecognitionViewBitmap -> {
                    recognitionView.visibility = View.VISIBLE
                    chooseButton.visibility = View.INVISIBLE
                    recognitionView.showSelectedImage(image = viewState.image)
                }
                is RecognitionViewStates.RecognitionStarted ->
                    recognitionView.showLoading()
                is RecognitionViewStates.CloseRecognitionView -> {
                    recognitionView.hideLoading()
                    recognitionView.visibility = View.GONE
                    chooseButton.visibility = View.VISIBLE
                }
                is RecognitionViewStates.ImageRecognized -> {
                    recognitionView.hideLoading()
                    recognitionView.imageRecognized(predictions = viewState.predictions)
                }
                is RecognitionViewStates.ShowLiveRecognitionView -> {
                    cameraView.visibility = View.VISIBLE
                    cameraView.showCamera()
                    overlayView.visibility = View.VISIBLE
                    overlayView.invalidate()
                }
                is RecognitionViewStates.LiveImageRecognized -> {
                    cameraView.setResultToSession(detections = viewState.detections)

                    val tracker = MultiBoxTracker(cameraView.context)
                    tracker.setFrameConfiguration(
                        viewState.image.width,
                        viewState.image.height,
                        viewState.orientation
                    );
                    overlayView.tracker = tracker

                    val mappedRecognitions: MutableList<Detector.RecognitionDetection> =
                        ArrayList<Detector.RecognitionDetection>()

                    for (result in viewState.detections!!) {
                        val location: RectF = result!!.getLocation()
                        if (result!!.confidence!! >= 0.5F) {
                            viewState.matrix.mapRect(location)
                            result.setLocation(location)
                            mappedRecognitions.add(result)
                        }
                    }
                    tracker.trackResults(mappedRecognitions)
                    overlayView.invalidate()
                }
                RecognitionViewStates.CloseLiveRecognitionView -> {
                    cameraView.visibility = View.GONE
                    cameraView.hideCamera()
                    overlayView.visibility = View.GONE
                }
                is RecognitionViewStates.ImageSaved -> {

                }
                is RecognitionViewStates.ImageSavedBitmap -> {
                    saveImage(viewState.image, context = activity, viewState.name)
                }
            }
        }
    }

    /// @param folderName can be your app's name
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
                showImageSaved()
            }
            else
                showImageNotSaved()
        } else {
            val directory = File(
                Environment.getExternalStorageDirectory().toString() + separator + folderName
            )
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                showImageSaved()
            }
            else
                showImageNotSaved()
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showImageSaved(){

            val builder = AlertDialog.Builder(activity)

            with(builder)
            {
                setTitle(activity.getString(R.string.save))
                setMessage(activity.getString(R.string.saved))
                setCancelable(false)
                setPositiveButton("ok") { _, _ ->

                }
                show()
            }
    }

    private fun showImageNotSaved(){

        val builder = AlertDialog.Builder(activity)

        with(builder)
        {
            setTitle(activity.getString(R.string.save))
            setMessage(activity.getString(R.string.save_error))
            setCancelable(false)
            setPositiveButton("ok") { _, _ ->

            }
            show()
        }
    }
}