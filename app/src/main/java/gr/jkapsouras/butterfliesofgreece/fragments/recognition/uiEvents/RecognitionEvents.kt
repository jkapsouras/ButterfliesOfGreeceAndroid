package gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents

import android.media.Image
import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class RecognitionEvents : UiEvent() {
    object OnlineClicked : RecognitionEvents()
    object OfflineClicked: RecognitionEvents()
    object ChoosePhotoClicked: RecognitionEvents()
    class PhotoChosen(image:Image): RecognitionEvents()
    object TakePhotoClicked: RecognitionEvents()
    class PhotoTaken(image:Image): RecognitionEvents()
    object LiveRecognitionClicked: RecognitionEvents()
    object CloseClicked: RecognitionEvents()
    object CloseLiveClicked: RecognitionEvents()
    class LiveImageTaken(image:Image): RecognitionEvents()
}