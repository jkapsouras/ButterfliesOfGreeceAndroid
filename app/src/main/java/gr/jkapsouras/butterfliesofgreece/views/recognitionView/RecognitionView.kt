package gr.jkapsouras.butterfliesofgreece.views.recognitionView

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ViewRecognitionBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.Prediction
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RecognitionView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr){
    private var _binding: ViewRecognitionBinding? = null
    private val binding get() = _binding!!
    lateinit var view: View
    private val emitter: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()

    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        _binding = ViewRecognitionBinding.inflate(LayoutInflater.from(context), this, true)
        view = binding.root

    }

    private fun viewEvents(): Observable<UiEvent> {

        binding.buttonOnlineRecognition.setOnClickListener {
            emitter.onNext(RecognitionEvents.OnlineClicked)
        }

        binding.buttonOfflineRecognition.setOnClickListener {
            emitter.onNext(RecognitionEvents.OfflineClicked)
        }

        binding.buttonCloseRecognitionView.setOnClickListener {
            emitter.onNext(RecognitionEvents.CloseClicked)
        }

        binding.buttonSave.setOnClickListener {
            emitter.onNext(RecognitionEvents.SaveImage(false))
        }

        return emitter
    }

    fun showSelectedImage(image: Uri){
       binding.imageRecognized.setImageURI(image)
        binding.buttonOnlineRecognition.visibility = View.VISIBLE
        binding.buttonOfflineRecognition.visibility = View.VISIBLE
        binding.labelRecognized.text = ""
        binding.labelRecognized.visibility = View.GONE
        binding.buttonSave.visibility = View.GONE
    }

    fun showSelectedImage(image: Bitmap){
        binding.imageRecognized.setImageBitmap(image)
        binding.buttonOnlineRecognition.visibility = View.VISIBLE
        binding.buttonOfflineRecognition.visibility = View.VISIBLE
        binding.labelRecognized.text = ""
        binding.labelRecognized.visibility = View.GONE
        binding.buttonSave.visibility = View.GONE
    }

    fun showLoading() {
        binding.loadingRecognition.visibility = View.VISIBLE
        binding.buttonOnlineRecognition.visibility = View.GONE
        binding.buttonOfflineRecognition.visibility = View.GONE
    }

    fun hideLoading(){
        binding.loadingRecognition.visibility = View.GONE
        binding.buttonOnlineRecognition.visibility = View.VISIBLE
        binding.buttonOfflineRecognition.visibility = View.VISIBLE
    }

    fun imageRecognized(predictions:List<Prediction>) {
        binding.buttonOnlineRecognition.visibility = View.VISIBLE
        binding.buttonOfflineRecognition.visibility = View.VISIBLE
        binding.labelRecognized.visibility = View.VISIBLE
        binding.labelRecognized.text =
            "${binding.labelRecognized.context.getString(R.string.photo_recognized_first)} ${predictions[0].butterflyClass}"//" \(Translations.RecognizedSecond) \(predictions[0].prob)%"
        binding.buttonSave.visibility = View.VISIBLE
    }
}