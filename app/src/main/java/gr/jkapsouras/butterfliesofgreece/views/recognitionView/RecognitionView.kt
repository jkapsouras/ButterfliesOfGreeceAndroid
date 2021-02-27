package gr.jkapsouras.butterfliesofgreece.views.recognitionView

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.Prediction
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.uiEvents.RecognitionEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_recognition.view.*

class RecognitionView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr){

    lateinit var view: View
    private val emitter: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()

    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        view = LayoutInflater.from(context)
            .inflate(R.layout.view_recognition, this)

    }

    private fun viewEvents(): Observable<UiEvent> {

        button_online_recognition.setOnClickListener {
            emitter.onNext(RecognitionEvents.OnlineClicked)
        }

        button_offline_recognition.setOnClickListener {
            emitter.onNext(RecognitionEvents.OfflineClicked)
        }

        button_close_recognition_view.setOnClickListener {
            emitter.onNext(RecognitionEvents.CloseClicked)
        }

        button_save.setOnClickListener {
            emitter.onNext(RecognitionEvents.SaveImage)
        }

        return emitter
    }

    fun showSelectedImage(image: Uri){
        image_recognized.setImageURI(image)
        button_online_recognition.visibility = View.VISIBLE
        button_offline_recognition.visibility = View.VISIBLE
        label_recognized.text = ""
        label_recognized.visibility = View.GONE
        button_save.visibility = View.GONE
    }

    fun showSelectedImage(image: Bitmap){
        image_recognized.setImageBitmap(image)
        button_online_recognition.visibility = View.VISIBLE
        button_offline_recognition.visibility = View.VISIBLE
        label_recognized.text = ""
        label_recognized.visibility = View.GONE
        button_save.visibility = View.GONE
    }

    fun showLoading() {
        loading_recognition.visibility = View.VISIBLE
        button_online_recognition.visibility = View.GONE
        button_offline_recognition.visibility = View.GONE
    }

    fun hideLoading(){
        loading_recognition.visibility = View.GONE
        button_online_recognition.visibility = View.VISIBLE
        button_offline_recognition.visibility = View.VISIBLE
    }

    fun imageRecognized(predictions:List<Prediction>) {
        button_online_recognition.visibility = View.VISIBLE
        button_offline_recognition.visibility = View.VISIBLE
        label_recognized.visibility = View.VISIBLE
        label_recognized.text =
            "${label_recognized.context.getString(R.string.photo_recognized_first)} ${predictions[0].butterflyClass}"//" \(Translations.RecognizedSecond) \(predictions[0].prob)%"
        button_save.visibility = View.VISIBLE
    }
}