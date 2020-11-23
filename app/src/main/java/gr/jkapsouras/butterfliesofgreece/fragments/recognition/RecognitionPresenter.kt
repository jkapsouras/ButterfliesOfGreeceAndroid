package gr.jkapsouras.butterfliesofgreece.fragments.recognition

import android.util.Log
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.dto.Avatar
import gr.jkapsouras.butterfliesofgreece.dto.BAvatar
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.state.RecognitionState
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.Permissions
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.viewStates.RecognitionViewStates
import gr.jkapsouras.butterfliesofgreece.managers.LocationManager.Companion.TAG
import gr.jkapsouras.butterfliesofgreece.repositories.RecognitionRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.File
import java.util.*

class RecognitionPresenter(
    val recognitionRepository: RecognitionRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    var recognitionState:RecognitionState = RecognitionState(null, null, emptyList())
//    private var modelDataHandler: ModelDataHandler = ModelDataHandler()
//    var processing = false

    fun setActivity(activity: MainActivity)
    {
       recognitionRepository.activity = activity
    }

    override fun setupEvents() {

    }

    override fun handleEvent(uiEvents: UiEvent) {
        when(uiEvents) {
            is RecognitionEvents ->
                handleRecognitionEvents(recognitionEvent = uiEvents)
        }
    }

     private fun handleRecognitionEvents(recognitionEvent: RecognitionEvents) {
         when (recognitionEvent) {
             RecognitionEvents.ChoosePhotoClicked ->
                 state.onNext(RecognitionViewStates.ShowGallery)
             RecognitionEvents.TakePhotoClicked ->
                 state.onNext(RecognitionViewStates.ShowCamera)
             is RecognitionEvents.PermissionGranted ->
                 when (recognitionEvent.permission) {
                     Permissions.Gallery ->
                     state.onNext(RecognitionViewStates.ShowGallery)
                     Permissions.Camera ->
                         state.onNext(RecognitionViewStates.ShowCamera)
                 }
             RecognitionEvents.PermissionDenied ->
                 state.onNext(RecognitionViewStates.ShowPermissionDenied)
             is RecognitionEvents.PhotoChosen -> {
                 if (recognitionEvent.image != null) {
                     recognitionState = recognitionState.with(imageData = recognitionEvent.image)
                     state.onNext(RecognitionViewStates.ShowRecognitionView(recognitionState.imageData!!))
                 }
             }
             is RecognitionEvents.PhotoTaken ->{
                 recognitionState = recognitionState.with(image = recognitionEvent.image)
                 state.onNext(RecognitionViewStates.ShowRecognitionViewBitmap(recognitionState.image!!))
             }
             RecognitionEvents.OnlineClicked -> {
                         state.onNext(RecognitionViewStates.RecognitionStarted)
                 if(recognitionState.imageData!=null) {
                     recognitionRepository.recognize(Avatar(recognitionState.imageData!!))
                         .subscribeOn(backgroundThreadScheduler.scheduler)
                         .subscribeBy(onNext = { predictions ->
                             recognitionState =
                                 recognitionState.with(predictions = predictions.predictions)
                             state.onNext(RecognitionViewStates.ImageRecognized(predictions = recognitionState.predictions))
                         }, onError = {
                             Log.d(TAG, "handleRecognitionEvents: ${it.localizedMessage}")
                         })
                         .disposeWith(disposables)
                 }
                 else if(recognitionState.image!=null){
                     recognitionRepository.recognize(BAvatar(recognitionState.image!!))
                         .subscribeOn(backgroundThreadScheduler.scheduler)
                         .subscribeBy(onNext = { predictions ->
                             recognitionState =
                                 recognitionState.with(predictions = predictions.predictions)
                             state.onNext(RecognitionViewStates.ImageRecognized(predictions = recognitionState.predictions))
                         }, onError = {
                             Log.d(TAG, "handleRecognitionEvents: ${it.localizedMessage}")
                         })
                         .disposeWith(disposables)
                 }
             }
             RecognitionEvents.CloseClicked ->
                 state.onNext(RecognitionViewStates.CloseRecognitionView)
         }
     }
}