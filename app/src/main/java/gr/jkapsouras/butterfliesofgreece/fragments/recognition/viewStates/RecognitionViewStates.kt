package gr.jkapsouras.butterfliesofgreece.fragments.recognition.viewStates

import android.media.Image
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.dto.Prediction

sealed class RecognitionViewStates(isTransition:Boolean) : ViewState(isTransition) {
    object ShowGallery : RecognitionViewStates(false)
    object ShowCamera : RecognitionViewStates(false)
    class ImageRecognized(predictions:List<Prediction>) : RecognitionViewStates(false)
    class LiveImageRecognized(predictions:List<Prediction>) : RecognitionViewStates(false)
    class ShowRecognitionView(image: Image) : RecognitionViewStates(false)
    object RecognitionStarted : RecognitionViewStates(false)
    object CloseRecognitionView : RecognitionViewStates(false)
    object ShowLiveRecognitionView : RecognitionViewStates(false)
    object CloseLiveRecognitionView : RecognitionViewStates(false)
}