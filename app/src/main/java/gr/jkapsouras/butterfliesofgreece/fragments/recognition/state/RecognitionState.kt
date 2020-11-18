package gr.jkapsouras.butterfliesofgreece.fragments.recognition.state

import android.media.Image
import gr.jkapsouras.butterfliesofgreece.dto.Prediction

class RecognitionState (
    val imageData:String?,
    val image: Image?,
    val predictions:List<Prediction>){
}

    fun RecognitionState.with(image:Image? = null, imageData:String? = null, predictions:List<Prediction>? = null) : RecognitionState {
        return RecognitionState(
            image = image ?: this.image,
            imageData = imageData ?: this.imageData,
            predictions = predictions ?: this.predictions
        )
    }
