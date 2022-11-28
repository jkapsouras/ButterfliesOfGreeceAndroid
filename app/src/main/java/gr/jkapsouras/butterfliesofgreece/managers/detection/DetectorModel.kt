package gr.jkapsouras.butterfliesofgreece.managers.detection

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Trace
import org.tensorflow.lite.Interpreter
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.min

/**
 * Wrapper for frozen detection models trained using the Tensorflow Object Detection API: -
 * https://github.com/tensorflow/models/tree/master/research/object_detection where you can find the
 * training code.
 *
 *
 * To use pretrained models in the API or convert to TF Lite models, please see docs for details:
 * -
 * https://github.com/tensorflow/models/blob/master/research/object_detection/g3doc/tf1_detection_zoo.md
 * -
 * https://github.com/tensorflow/models/blob/master/research/object_detection/g3doc/tf2_detection_zoo.md
 * -
 * https://github.com/tensorflow/models/blob/master/research/object_detection/g3doc/running_on_mobile_tensorflowlite.md#running-our-model-on-android
 */
class TFLiteObjectDetectionAPIModel private constructor() : Detector {
    private var isModelQuantized = false

    // Config values.
    private var inputSize = 0

    // Pre-allocated buffers.
    private val labels: MutableList<String> = ArrayList()
    private var intValues: IntArray? = null

    // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
    // contains the location of detected boxes
    private var outputLocations: Array<Array<FloatArray>>? = null

    // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the classes of detected boxes
    private var outputClasses: Array<FloatArray>? = null

    // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the scores of detected boxes
    private var outputScores: Array<FloatArray>? = null

    // numDetections: array of shape [Batchsize]
    // contains the number of detected boxes
    private var numDetections: FloatArray? = null
    private var imgData: ByteBuffer? = null
    private var tfLiteModel: MappedByteBuffer? = null
    private var tfLiteOptions: Interpreter.Options? = null
    private var tfLite: Interpreter? = null
    override  fun recognizeImage(bitmap: Bitmap?): List<Detector.RecognitionDetection?>?{
        // Log this method so that it can be analyzed with systrace.
        Trace.beginSection("recognizeImage")
        Trace.beginSection("preprocessBitmap")
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        bitmap!!.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        imgData!!.rewind()
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue = intValues!![i * inputSize + j]
                if (isModelQuantized) {
                    // Quantized model
                    imgData!!.put((pixelValue shr 16 and 0xFF).toByte())
                    imgData!!.put((pixelValue shr 8 and 0xFF).toByte())
                    imgData!!.put((pixelValue and 0xFF).toByte())
                } else { // Float model
                    imgData!!.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData!!.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData!!.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                }
            }
        }
        Trace.endSection() // preprocessBitmap

        // Copy the input data into TensorFlow.
        Trace.beginSection("feed")
        outputLocations = Array(1) {
            Array(NUM_DETECTIONS) {
                FloatArray(
                    4
                )
            }
        }
        outputClasses = Array(1) {
            FloatArray(
                NUM_DETECTIONS
            )
        }
        outputScores = Array(1) {
            FloatArray(
                NUM_DETECTIONS
            )
        }
        numDetections = FloatArray(1)
        val inputArray = arrayOf<Any>(imgData!!)
        val outputMap: MutableMap<Int, Any> = HashMap()
        outputMap[0] = outputLocations!!
        outputMap[1] = outputClasses!!
        outputMap[2] = outputScores!!
        outputMap[3] = numDetections!!
        Trace.endSection()

        // Run the inference call.
        Trace.beginSection("run")
        tfLite!!.runForMultipleInputsOutputs(inputArray, outputMap)
        Trace.endSection()

        // Show the best detections.
        // after scaling them back to the input size.
        // You need to use the number of detections from the output and not the NUM_DETECTONS variable
        // declared on top
        // because on some models, they don't always output the same total number of detections
        // For example, your model's NUM_DETECTIONS = 20, but sometimes it only outputs 16 predictions
        // If you don't use the output's numDetections, you'll get nonsensical data
        val numDetectionsOutput: Int = min(
            NUM_DETECTIONS,
            numDetections!![0].toInt()
        ) // cast from float to integer, use min for safety
        val recognitions: ArrayList<Detector.RecognitionDetection> = ArrayList(numDetectionsOutput)
        for (i in 0 until numDetectionsOutput) {
            val detection = RectF(
                outputLocations!![0][i][1] * inputSize,
                outputLocations!![0][i][0] * inputSize,
                outputLocations!![0][i][3] * inputSize,
                outputLocations!![0][i][2] * inputSize
            )
            recognitions.add(
                Detector.RecognitionDetection(
                    "" + i, labels[outputClasses!![0][i].toInt() + 1], outputScores!![0][i],false, detection
                )
            )
        }
        Trace.endSection() // "recognizeImage"
        return recognitions
    }

    override fun enableStatLogging(logStats: Boolean) {}
    override  val statString: String
        get() = ""

    override  fun close() {
        if (tfLite != null) {
            tfLite!!.close()
            tfLite = null
        }
    }

    override  fun setNumThreads(numThreads: Int) {
        if (tfLite != null) {
            tfLiteOptions!!.setNumThreads(numThreads)
            recreateInterpreter()
        }
    }

    override  fun setUseNNAPI(isChecked: Boolean) {
        if (tfLite != null) {
            tfLiteOptions!!.setUseNNAPI(isChecked)
            recreateInterpreter()
        }
    }

    private fun recreateInterpreter() {
        tfLite!!.close()
        tfLite = Interpreter(tfLiteModel!!, tfLiteOptions)
    }

    companion object {
        private const val TAG = "TFLiteObjectDetectionAPIModelWithInterpreter"

        // Only return this many results.
        private const val NUM_DETECTIONS = 10

        // Float model
        private const val IMAGE_MEAN = 127.5f
        private const val IMAGE_STD = 127.5f

        // Number of threads in the java app
        private const val NUM_THREADS = 4

        /** Memory-map the model file in Assets.  */
        @Throws(IOException::class)
        private fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
            val fileDescriptor = assets.openFd(modelFilename)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel: FileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }

        /**
         * Initializes a native TensorFlow session for classifying images.
         *
         * @param modelFilename The model file path relative to the assets folder
         * @param labelFilename The label file path relative to the assets folder
         * @param inputSize The size of image input
         * @param isQuantized Boolean representing model is quantized or not
         */
        @Throws(IOException::class)
        fun create(
            context: Context,
            modelFilename: String,
            labelFilename: String?,
            inputSize: Int,
            isQuantized: Boolean
        ): Detector {
            val d = TFLiteObjectDetectionAPIModel()
            val modelFile: MappedByteBuffer = loadModelFile(context.assets, modelFilename)

            val x = assetFilePath(context, "labelmap.txt")

            val inputStream: InputStream = File(x).inputStream()
            val lineList = mutableListOf<String>()

            inputStream.bufferedReader().forEachLine { lineList.add(it) }
//            lineList.forEach{println(">  " + it)}

            for(line in lineList)
            {
                d.labels.add(line)
            }
//            val metadata = MetadataExtractor(modelFile)
//            BufferedReader(
//                InputStreamReader(
//                    metadata.getAssociatedFile(labelFilename), Charset.defaultCharset()
//                )
//            ).use { br ->
//                var line: String
//                while (br.readLine().also { line = it } != null) {
//                    Log.w("TAG", line)
//                    d.labels.add(line)
//                }
//            }
            d.inputSize = inputSize
            try {
                val options: Interpreter.Options = Interpreter.Options()
                options.setNumThreads(NUM_THREADS)
                options.setUseXNNPACK(true)
                d.tfLite = Interpreter(modelFile, options)
                d.tfLiteModel = modelFile
                d.tfLiteOptions = options
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
            d.isModelQuantized = isQuantized
            // Pre-allocate buffers.
            val numBytesPerChannel: Int
            numBytesPerChannel = if (isQuantized) {
                1 // Quantized
            } else {
                4 // Floating point
            }
            d.imgData =
                ByteBuffer.allocateDirect(1 * d.inputSize * d.inputSize * 3 * numBytesPerChannel)
            d.imgData!!.order(ByteOrder.nativeOrder())
            d.intValues = IntArray(d.inputSize * d.inputSize)
            d.outputLocations = Array(1) {
                Array(NUM_DETECTIONS) {
                    FloatArray(
                        4
                    )
                }
            }
            d.outputClasses = Array(1) {
                FloatArray(
                    NUM_DETECTIONS
                )
            }
            d.outputScores = Array(1) {
                FloatArray(
                    NUM_DETECTIONS
                )
            }
            d.numDetections = FloatArray(1)
            return d
        }

        @Throws(IOException::class)
        fun assetFilePath(context: Context, assetName: String?): String? {
            val file = File(context.filesDir, assetName)
            if (file.exists() && file.length() > 0) {
                return file.absolutePath
            }
            context.assets.open(assetName!!).use { `is` ->
                FileOutputStream(file).use { os ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (`is`.read(buffer).also { read = it } != -1) {
                        os.write(buffer, 0, read)
                    }
                    os.flush()
                }
                return file.absolutePath
            }
        }
    }
}
