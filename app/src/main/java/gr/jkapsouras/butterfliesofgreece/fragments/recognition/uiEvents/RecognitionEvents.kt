package gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents

import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class RecognitionEvents : UiEvent() {
    object OnlineClicked : RecognitionEvents()
    object OfflineClicked: RecognitionEvents()
    object ChoosePhotoClicked: RecognitionEvents()
    class PhotoChosen(val image: Uri?): RecognitionEvents()
    object TakePhotoClicked: RecognitionEvents()
    class PhotoTaken(val image:Bitmap): RecognitionEvents()
    object LiveRecognitionClicked: RecognitionEvents()
    object CloseClicked: RecognitionEvents()
    object CloseLiveClicked: RecognitionEvents()
    class LiveImageTaken(image:Image): RecognitionEvents()
    object PermissionDenied : RecognitionEvents()
    class PermissionGranted(val permission: Permissions) : RecognitionEvents()
}

sealed class Permissions{
    object Camera : Permissions()
    object Gallery : Permissions()
}