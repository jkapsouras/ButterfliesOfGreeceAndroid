package gr.jkapsouras.butterfliesofgreece.managers.detection

import android.graphics.Bitmap
import android.graphics.RectF


/** Generic interface for interacting with different recognition engines.  */
interface Detector {
    fun recognizeImage(bitmap: Bitmap?): List<RecognitionDetection?>?
    fun enableStatLogging(debug: Boolean)
    val statString: String?

    fun close()
    fun setNumThreads(numThreads: Int)
    fun setUseNNAPI(isChecked: Boolean)

    /** An immutable result returned by a Detector describing what was recognized.  */
    class RecognitionDetection(
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        val id: String?,
        /** Display name for the recognition.  */
        val title: String?,
        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        val confidence: Float?,
        val isEndangered:Boolean,
        /** Optional location within the source image for the location of the recognized object.  */
        private var location: RectF?
    ) {

        fun getLocation(): RectF {
            return RectF(location)
        }

        fun setLocation(location: RectF?) {
            this.location = location
        }

        override fun toString(): String {
            var resultString = ""
            if (id != null) {
                resultString += "[$id] "
            }
            if (title != null) {
                resultString += "$title "
            }
            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f)
            }
            if (location != null) {
                resultString += location.toString() + " "
            }
            return resultString.trim { it <= ' ' }
        }
    }
}