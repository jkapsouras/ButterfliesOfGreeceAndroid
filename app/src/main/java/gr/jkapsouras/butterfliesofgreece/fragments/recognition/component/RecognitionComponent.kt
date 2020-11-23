package gr.jkapsouras.butterfliesofgreece.fragments.recognition.component

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
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
import gr.jkapsouras.butterfliesofgreece.views.recognitionView.RecognitionView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RecognitionComponent(private val activity:MainActivity, private val chooseButton: Button, private val recognitionView: RecognitionView) : UiComponent {

    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {

        chooseButton.setOnClickListener {
            event.onNext(RecognitionEvents.ChoosePhotoClicked)
            Log.d(Constraints.TAG, "choose photo clicked")
        }

        uiEvents = Observable.merge(recognitionView.uiEvents, event, activity.emitterEvents)

        recognitionView.visibility = View.GONE
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(activity, intent, 1000, null)
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
                is RecognitionViewStates.ShowPermissionDenied ->{
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                }
                is RecognitionViewStates.ShowRecognitionView ->{
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
                else ->
                    print("default state")
            }
        }
    }
}