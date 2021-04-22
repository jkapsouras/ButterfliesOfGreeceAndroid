package gr.jkapsouras.butterfliesofgreece.managers.detection

import android.graphics.Bitmap
import android.widget.Toast
import gr.jkapsouras.butterfliesofgreece.MainActivity
import java.io.IOException


class DetectionManager {
    private val TF_OD_API_INPUT_SIZE = 300
    private val TF_OD_API_IS_QUANTIZED = false
    private val TF_OD_API_MODEL_FILE = "detect.tflite"
    private val TF_OD_API_LABELS_FILE = "labelmap.txt"

    lateinit var detector : Detector
    lateinit var bitmap: Bitmap
    lateinit var activity: MainActivity

    fun recognizeImage(): List<Detector.RecognitionDetection?>?
    {
        return detector.recognizeImage((bitmap))
    }

    fun createDetector (){
        var cropSize = TF_OD_API_INPUT_SIZE
        try {
            detector = TFLiteObjectDetectionAPIModel.create(
                activity,
                TF_OD_API_MODEL_FILE,
                TF_OD_API_LABELS_FILE,
                TF_OD_API_INPUT_SIZE,
                TF_OD_API_IS_QUANTIZED
            )
            cropSize = TF_OD_API_INPUT_SIZE
        } catch (e: IOException) {
            e.printStackTrace()
            val toast = Toast.makeText(
                activity, "Detector could not be initialized", Toast.LENGTH_SHORT
            )
            toast.show()
//            application.finish()
        }
    }
}