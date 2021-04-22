package gr.jkapsouras.butterfliesofgreece.fragments.recognition.viewStates

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.net.Uri
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.dto.Prediction
import gr.jkapsouras.butterfliesofgreece.managers.detection.Detector

sealed class RecognitionViewStates(isTransition:Boolean) : ViewState(isTransition) {
    object ShowGallery : RecognitionViewStates(false)
    object ShowCamera : RecognitionViewStates(false)
    class ImageRecognized(val predictions:List<Prediction>) : RecognitionViewStates(false)
    class LiveImageRecognized(val detections:List<Detector.RecognitionDetection?>?, val orientation:Int, val image: Bitmap, val matrix: Matrix) : RecognitionViewStates(false)
    class ShowRecognitionView(val image: Uri) : RecognitionViewStates(false)
    class ShowRecognitionViewBitmap(val image: Bitmap) : RecognitionViewStates(false)
    object RecognitionStarted : RecognitionViewStates(false)
    object CloseRecognitionView : RecognitionViewStates(false)
    object ShowLiveRecognitionView : RecognitionViewStates(false)
    object CloseLiveRecognitionView : RecognitionViewStates(false)
    object ShowPermissionDenied : RecognitionViewStates(false)
    class ImageSaved(val image: Uri, val name: String) : RecognitionViewStates(false)
    class ImageSavedBitmap(val image: Bitmap, val name: String) : RecognitionViewStates(false)
}
