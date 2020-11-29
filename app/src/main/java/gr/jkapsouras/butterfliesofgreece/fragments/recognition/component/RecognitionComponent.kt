package gr.jkapsouras.butterfliesofgreece.fragments.recognition.component

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints
import androidx.core.app.ActivityCompat.startActivityForResult
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.main.events.MenuUiEvents
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.viewStates.RecognitionViewStates
import gr.jkapsouras.butterfliesofgreece.managers.LocationManager.Companion.TAG
import gr.jkapsouras.butterfliesofgreece.views.cameraView.CameraView
import gr.jkapsouras.butterfliesofgreece.views.recognitionView.RecognitionView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RecognitionComponent(private val activity:MainActivity, private val chooseButton: Button, private val takePhotoButton: Button,
                           private val liveSessionButton: Button, private val recognitionView: RecognitionView, private val cameraView: CameraView) : UiComponent {

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

        uiEvents = Observable.merge(recognitionView.uiEvents, event, activity.emitterEvents, cameraView.uiEvents)

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
        startActivityForResult(activity, cameraIntent, 200, null)
    }

    override fun renderViewState(viewState: ViewState) {
        if (viewState is RecognitionViewStates) {
            when (viewState) {
                is RecognitionViewStates.ShowGallery -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                            //permission denied
                            val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                            //show popup to request runtime permission
                            activity.requestPermissions(permissions, 1001);
                        }
                        else{
                            //permission already granted
                            pickImageFromGallery();
                        }
                    }
                    else{
                        //system OS is < Marshmallow
                        pickImageFromGallery();
                    }
                }
                is RecognitionViewStates.ShowCamera ->{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (activity.checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED){
                            //permission denied
                            val permissions = arrayOf(Manifest.permission.CAMERA);
                            //show popup to request runtime permission
                            activity.requestPermissions(permissions, 201);
                        }
                        else{
                            //permission already granted
                            pickImageFromCamera();
                        }
                    }
                    else{
                        //system OS is < Marshmallow
                        pickImageFromCamera();
                    }
                }
                is RecognitionViewStates.ShowPermissionDenied ->{
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                is RecognitionViewStates.ShowRecognitionView ->{
                    recognitionView.visibility = View.VISIBLE
                    chooseButton.visibility = View.INVISIBLE
                    recognitionView.showSelectedImage(image = viewState.image)
                }
                is RecognitionViewStates.ShowRecognitionViewBitmap ->{
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
                }
                is RecognitionViewStates.LiveImageRecognized -> {
                    cameraView.setResultToSession(predictions = viewState.predictions)
                }
                RecognitionViewStates.CloseLiveRecognitionView -> {
                    cameraView.visibility = View.GONE
                    cameraView.hideCamera()
                }
            }
        }
    }
}