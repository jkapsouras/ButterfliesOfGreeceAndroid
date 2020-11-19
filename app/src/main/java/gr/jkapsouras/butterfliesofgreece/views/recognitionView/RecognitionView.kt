package gr.jkapsouras.butterfliesofgreece.views.recognitionView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RecognitionView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr){

    lateinit var view: View
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()

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
        return Observable.never()
    }
}