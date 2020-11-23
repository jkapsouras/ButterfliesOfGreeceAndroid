package gr.jkapsouras.butterfliesofgreece.fragments.recognition.state

import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import gr.jkapsouras.butterfliesofgreece.dto.Prediction
import java.io.ByteArrayOutputStream

class RecognitionState (
    val imageData: Uri?,
    val image: Bitmap?,
    val predictions:List<Prediction>){
}

    fun RecognitionState.with(image:Bitmap? = null, imageData:Uri? = null, predictions:List<Prediction>? = null) : RecognitionState {
        return when {
            image!=null -> RecognitionState(image = image, imageData =  null, predictions =  predictions ?: this.predictions)
            imageData!=null -> RecognitionState(image = null, imageData =  imageData, predictions =  predictions ?: this.predictions)
            else -> RecognitionState(
                image = image ?: this.image,
                imageData = imageData ?: this.imageData,
                predictions = predictions ?: this.predictions
            )
        }
    }
