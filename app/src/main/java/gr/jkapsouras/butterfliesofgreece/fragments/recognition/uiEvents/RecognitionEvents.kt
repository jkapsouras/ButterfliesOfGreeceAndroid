package gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents

import android.graphics.Bitmap
import android.graphics.Matrix
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
    class LiveImageTaken(val initBitmap: Bitmap, val image:Bitmap, val orientation: Int, val cropToFrameTransform: Matrix): RecognitionEvents()
    object PermissionDenied : RecognitionEvents()
    class PermissionGranted(val permission: Permissions) : RecognitionEvents()
    class SaveImage(val fromLive : Boolean):RecognitionEvents()
}

sealed class Permissions{
    object Camera : Permissions()
    object Gallery : Permissions()
    object LiveSession : Permissions()
}