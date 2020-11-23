package gr.jkapsouras.butterfliesofgreece.fragments.recognition.state

import android.media.Image
import android.net.Uri
import gr.jkapsouras.butterfliesofgreece.dto.Prediction
import java.io.ByteArrayOutputStream

class RecognitionState (
    val imageData: Uri?,
    val image: Image?,
    val predictions:List<Prediction>){
}

    fun RecognitionState.with(image:Image? = null, imageData:Uri? = null, predictions:List<Prediction>? = null) : RecognitionState {
        return RecognitionState(
            image = image ?: this.image,
            imageData = imageData ?: this.imageData,
            predictions = predictions ?: this.predictions
        )
    }
