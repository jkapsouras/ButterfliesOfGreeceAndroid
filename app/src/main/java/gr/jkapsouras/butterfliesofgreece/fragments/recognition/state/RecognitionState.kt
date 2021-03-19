package gr.jkapsouras.butterfliesofgreece.fragments.recognition.state

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.net.Uri
import gr.jkapsouras.butterfliesofgreece.dto.Prediction
import gr.jkapsouras.butterfliesofgreece.managers.detection.Detector
import java.io.ByteArrayOutputStream

class RecognitionState (
    val imageData: Uri?,
    val initImage: Bitmap?,
    val image: Bitmap?,
    val imageOrientation: Int,
    val matrix: Matrix,
    val predictions:List<Prediction>,
    val detections:List<Detector.RecognitionDetection?>?){
}

    fun RecognitionState.with(initImage: Bitmap? = null,  image:Bitmap? = null, matrix: Matrix? = null, imageData:Uri? = null, imageOrientation: Int? = null, predictions:List<Prediction>? = null, detections: List<Detector.RecognitionDetection?>? = null, width:Int? = null, height:Int? = null) : RecognitionState {
        return when {
            image!=null ->
                RecognitionState(initImage = initImage, image = image, imageData =  null, predictions =  predictions ?: this.predictions, detections = detections ?: this.detections, imageOrientation = imageOrientation ?: this.imageOrientation, matrix = matrix ?: this.matrix)
            imageData!=null ->
                RecognitionState(initImage = null, image = null, imageData =  imageData, predictions =  predictions ?: this.predictions,detections = detections ?: this.detections,  imageOrientation = imageOrientation ?: this.imageOrientation, matrix = matrix ?: this.matrix)
            else -> RecognitionState(
                initImage = initImage ?: this.initImage,
                image = image ?: this.image,
                imageData = imageData ?: this.imageData,
                predictions = predictions ?: this.predictions,
                detections = detections ?: this.detections,
                imageOrientation = imageOrientation ?: this.imageOrientation,
                matrix = matrix ?: this.matrix
            )
        }
    }
