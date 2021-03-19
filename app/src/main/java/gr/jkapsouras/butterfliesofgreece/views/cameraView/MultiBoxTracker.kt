package gr.jkapsouras.butterfliesofgreece.views.cameraView

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Cap
import android.graphics.Paint.Join
import android.text.TextUtils
import android.util.TypedValue
import gr.jkapsouras.butterfliesofgreece.managers.detection.Detector
import gr.jkapsouras.butterfliesofgreece.utils.ImageUtils.getTransformationMatrix
import java.util.*


class MultiBoxTracker(context: Context) {
    val screenRects: MutableList<Pair<Float, RectF>> = LinkedList<Pair<Float, RectF>>()
//    private val logger: Logger = Logger()
    private val availableColors: Queue<Int> = LinkedList<Int>()
    private val trackedObjects: MutableList<TrackedRecognition> = LinkedList<TrackedRecognition>()
    private val boxPaint: Paint = Paint()
    private val textSizePx: Float
    private val borderedText: BorderedText
    private var frameToCanvasMatrix: Matrix? = null
    private var frameWidth = 0
    private var frameHeight = 0
    private var sensorOrientation = 0
    @Synchronized
    fun setFrameConfiguration(
        width: Int, height: Int, sensorOrientation: Int
    ) {
        frameWidth = width
        frameHeight = height
        this.sensorOrientation = sensorOrientation
    }

    @Synchronized
    fun drawDebug(canvas: Canvas) {
        val textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 60.0f
        val boxPaint = Paint()
        boxPaint.color = Color.RED
        boxPaint.alpha = 200
        boxPaint.style = Paint.Style.STROKE
        for ((first, rect) in screenRects) {
            canvas.drawRect(rect, boxPaint)
            canvas.drawText("" + first, rect.left, rect.top, textPaint)
            borderedText.drawText(canvas, rect.centerX(), rect.centerY(), "" + first)
        }
    }

    @Synchronized
    fun trackResults(results: List<Detector.RecognitionDetection>) {
//        logger.i("Processing %d results from %d", results.size, timestamp)
        processResults(results)
    }

    private fun getFrameToCanvasMatrix(): Matrix? {
        return frameToCanvasMatrix
    }

    @Synchronized
    fun draw(canvas: Canvas) {
        val rotated = sensorOrientation % 180 == 90
        val multiplier: Float = Math.min(
            canvas.height / (if (rotated) frameWidth else frameHeight).toFloat(),
            canvas.width / (if (rotated) frameHeight else frameWidth).toFloat()
        )
        frameToCanvasMatrix = getTransformationMatrix(
            frameWidth,
            frameHeight,
            (multiplier * if (rotated) frameHeight else frameWidth).toInt(),
            (multiplier * if (rotated) frameWidth else frameHeight).toInt(),
            sensorOrientation,
            true
        )
        for (recognition in trackedObjects) {
            val trackedPos = RectF(recognition.location)
            getFrameToCanvasMatrix()!!.mapRect(trackedPos)
            boxPaint.setColor(recognition.color)
            val cornerSize = Math.min(trackedPos.width(), trackedPos.height()) / 8.0f
//            canvas.drawRect(trackedPos,boxPaint)
            canvas.drawRoundRect(trackedPos, cornerSize, cornerSize, boxPaint)
            val labelString = if (!TextUtils.isEmpty(recognition.title)) String.format(
                "%s %.2f", recognition.title,
                100 * recognition.detectionConfidence
            ) else String.format("%.2f", 100 * recognition.detectionConfidence)
            //            borderedText.drawText(canvas, trackedPos.left + cornerSize, trackedPos.top,
            // labelString);
            if("$labelString%".contains("\n"))
            {
                val texts = "$labelString%".split("\n")
                borderedText.drawLines(
                    canvas, trackedPos.left + cornerSize, trackedPos.top, texts, boxPaint
                )
            }
            else {
                borderedText.drawText(
                    canvas, trackedPos.left + cornerSize, trackedPos.top, "$labelString%", boxPaint
                )
            }
        }
    }

    private fun processResults(results: List<Detector.RecognitionDetection>) {
        val rectsToTrack: MutableList<Pair<Float, Detector.RecognitionDetection>> =
            LinkedList<Pair<Float, Detector.RecognitionDetection>>()
        screenRects.clear()
        val rgbFrameToScreen = Matrix(getFrameToCanvasMatrix())
        for (result in results) {
            if (result.getLocation() == null) {
                continue
            }
            val detectionFrameRect = RectF(result.getLocation())
            val detectionScreenRect = RectF()
            rgbFrameToScreen.mapRect(detectionScreenRect, detectionFrameRect)
//            logger.v(
//                "Result! Frame: " + result.getLocation()
//                    .toString() + " mapped to screen:" + detectionScreenRect
//            )
            screenRects.add(Pair(result.confidence!!, detectionScreenRect))
            if (detectionFrameRect.width() < MIN_SIZE || detectionFrameRect.height() < MIN_SIZE) {
//                logger.w("Degenerate rectangle! $detectionFrameRect")
                continue
            }
            rectsToTrack.add(Pair<Float, Detector.RecognitionDetection>(result.confidence, result))
        }
        trackedObjects.clear()
        if (rectsToTrack.isEmpty()) {
//            logger.v("Nothing to track, aborting.")
            return
        }
        for ((first, second) in rectsToTrack) {
            val trackedRecognition = TrackedRecognition()
            trackedRecognition.detectionConfidence = first
            trackedRecognition.location = RectF(second.getLocation())
            trackedRecognition.title = second.title
            if(second.isEndangered) {
                trackedRecognition.color = COLORS[1]
            }
            else{
                trackedRecognition.color = COLORS[0]
            }
            trackedObjects.add(trackedRecognition)
            if (trackedObjects.size >= COLORS.size) {
                break
            }
        }
    }

    private class TrackedRecognition {
        var location: RectF? = null
        var detectionConfidence = 0f
        var color = 0
        var title: String? = null
    }

    companion object {
        private const val TEXT_SIZE_DIP = 18f
        private const val MIN_SIZE = 16.0f
        private val COLORS = intArrayOf(
            Color.BLUE,
            Color.RED,
//            Color.GREEN,
//            Color.YELLOW,
//            Color.CYAN,
//            Color.MAGENTA,
//            Color.WHITE,
//            Color.parseColor("#55FF55"),
//            Color.parseColor("#FFA500"),
//            Color.parseColor("#FF8888"),
//            Color.parseColor("#AAAAFF"),
//            Color.parseColor("#FFFFAA"),
//            Color.parseColor("#55AAAA"),
//            Color.parseColor("#AA33AA"),
//            Color.parseColor("#0D0068")
        )
    }

    init {
        for (color in COLORS) {
            availableColors.add(color)
        }
        boxPaint.color =Color.RED
        boxPaint.style=Paint.Style.STROKE
        boxPaint.strokeWidth=10.0f
        boxPaint.strokeCap=Cap.ROUND
        boxPaint.strokeJoin=Join.ROUND
        boxPaint.strokeMiter=100F
        textSizePx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, context.resources.displayMetrics
        )
        borderedText = BorderedText(textSizePx)
    }
}
