package gr.jkapsouras.butterfliesofgreece.views.cameraView

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ViewCameraBinding
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import gr.jkapsouras.butterfliesofgreece.utils.ImageUtils
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val emitter = PublishSubject.create<UiEvent>()
    lateinit var view : View
    private var _binding: ViewCameraBinding? = null
    private val binding get() = _binding!!
    lateinit var activity : MainActivity
    var counter = 0
    private lateinit var cameraExecutor: ExecutorService
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        _binding = ViewCameraBinding.inflate(LayoutInflater.from(context), this, true)
        view = binding.root
    }

    private fun viewEvents() : Observable<UiEvent>
    {
       binding.buttonCloseLiveRecognitionView.setOnClickListener {
            emitter.onNext(RecognitionEvents.CloseLiveClicked)
        }

        binding.buttonSaveCameraView.setOnClickListener {
            emitter.onNext(RecognitionEvents.SaveImage(true))
        }

        return emitter
    }

    fun showCamera()
    {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                MainActivity.REQUIRED_PERMISSIONS,
                MainActivity.REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun hideCamera(){
        cameraExecutor.shutdown()
        binding.buttonSaveCameraView.visibility = View.GONE
    }

    private fun startCamera() {
        counter = 0
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle ownercreateSurfaceProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewCameraPreview.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


//            imageCapture = ImageCapture.Builder()
//                .build()
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor, LuminosityAnalyzer(
                            emitter,
                            counter,
                            activity
                        )
                    )
//                    { luma ->
////                        Log.d(MainActivity.TAG, "Average luminosity: $luma")
//                        activity.runOnUiThread {
//                            view.label_live_recognized.text = "Average luminosity: $luma"
//                        }
//                    })
                }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    activity, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e("TAG", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(activity))
    }

    private fun allPermissionsGranted() = MainActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(activity.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    fun showSaveButton(){
        binding.buttonSaveCameraView.visibility = View.VISIBLE
    }

    fun hideSaveButton(){
        binding.buttonSaveCameraView.visibility = View.GONE
    }

    private class LuminosityAnalyzer(
        private val emitter: PublishSubject<UiEvent>,
        private var counter: Int,
        private val activity: MainActivity
    ) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val bitmap = image.toBitmap()

            val rotation = image.imageInfo.rotationDegrees
            val previewWidth = bitmap.width
            val previewHeight = bitmap.height
            val sensorOrientation =  rotation - getScreenOrientation()

            val frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                    previewWidth, previewHeight,
                    300, 300,
                    sensorOrientation, true
                )

            val cropToFrameTransform = Matrix()
            frameToCropTransform.invert(cropToFrameTransform)

            val croppedBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
            val canvas =  Canvas(croppedBitmap)
            canvas.drawBitmap(bitmap, frameToCropTransform, null)

            counter += 1

            if(counter>=25) {
                Log.i("tsg", "counter: $counter")
                emitter.onNext(RecognitionEvents.LiveImageTaken(bitmap,
                    croppedBitmap, sensorOrientation, cropToFrameTransform))
                counter = 0

//                ImageUtils.saveBitmap(bitmap!!, activity, "tempFolder");

            }

            image.close()
        }

        fun getScreenOrientation(): Int {
            return when (activity.windowManager.defaultDisplay.rotation) {
                Surface.ROTATION_270 -> 270
                Surface.ROTATION_180 -> 180
                Surface.ROTATION_90 -> 90
                else -> 0
            }
        }

        private fun ImageProxy.toBitmap(): Bitmap {
            val yBuffer = planes[0].buffer // Y
            val uBuffer = planes[1].buffer // U
            val vBuffer = planes[2].buffer // V

            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()

            val nv21 = ByteArray(ySize + uSize + vSize)

            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
            val imageBytes = out.toByteArray()
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        private fun ImageProxy.imageToBitmap(rotationDegrees: Float): Bitmap? {
//            assert(this.getFormat() === ImageFormat.NV21)

            // NV21 is a plane of 8 bit Y values followed by interleaved  Cb Cr
            val ib = ByteBuffer.allocate(this.height * this.width * 2)
            val y: ByteBuffer = this.planes[0].buffer
            val cr: ByteBuffer = this.planes[1].buffer
            val cb: ByteBuffer = this.planes[1].buffer
            ib.put(y)
            ib.put(cb)
            ib.put(cr)
            val yuvImage = YuvImage(
                ib.array(),
                ImageFormat.NV21, this.width, this.height, null
            )
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(
                Rect(
                    0, 0,
                    this.width, this.height
                ), 100, out
            )
            val imageBytes = out.toByteArray()
            val bm = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            var bitmap = bm

            // On android the camera rotation and the screen rotation
            // are off by 90 degrees, so if you are capturing an image
            // in "portrait" orientation, you'll need to rotate the image.
            if (rotationDegrees != 0f) {
                val matrix = Matrix()
                matrix.postRotate(rotationDegrees)
                val scaledBitmap = Bitmap.createScaledBitmap(
                    bm,
                    bm.width, bm.height, true
                )
                bitmap = Bitmap.createBitmap(
                    scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix, true
                )
            }
            return bitmap
        }
    }
}