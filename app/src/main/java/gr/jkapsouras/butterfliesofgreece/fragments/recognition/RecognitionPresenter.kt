package gr.jkapsouras.butterfliesofgreece.fragments.recognition

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.state.RecognitionState
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.viewStates.RecognitionViewStates
import gr.jkapsouras.butterfliesofgreece.repositories.RecognitionRepository

class RecognitionPresenter(recognitionRepository: RecognitionRepository,
    backgroundThreadScheduler: IBackgroundThread,
                           mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    var recognitionState:RecognitionState = RecognitionState("", null, emptyList())
//    var recognitionRepository:RecognitionRepository
//    let compressionQuality:CGFloat = 0.7
//    private var modelDataHandler: ModelDataHandler = ModelDataHandler()
//    var processing = false

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
         }
     }
}