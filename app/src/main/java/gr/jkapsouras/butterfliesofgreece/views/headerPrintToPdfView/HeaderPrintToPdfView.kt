package gr.jkapsouras.butterfliesofgreece.views.headerPrintToPdfView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.jakewharton.rxbinding4.view.clicks
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ViewHeaderPrintToPdfBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PdfArrange
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.uiEvents.PrintToPdfEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class HeaderPrintToPdfView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    private var _binding: ViewHeaderPrintToPdfBinding? = null
    private val binding get() = _binding!!
    private val arranges: List<PdfArrange>
    get() = listOf(PdfArrange.OnePerPage, PdfArrange.TwoPerPage, PdfArrange.FourPerPage, PdfArrange.SixPerPage)
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()

    lateinit var view: View
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        _binding = ViewHeaderPrintToPdfBinding.inflate(LayoutInflater.from(context), this, true)
        view = binding.root

        val adapter = ArrayAdapter<PdfArrange>(view.context, R.layout.spinner_text, arranges)
        binding.spinnerImagesPerPage.adapter = adapter
        binding.spinnerImagesPerPage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                event.onNext(PrintToPdfEvents.ArrangeSelected(arranges[position]))
            }

        }
    }

    private fun viewEvents(): Observable<UiEvent> {
        return Observable.merge(binding.ivDeleteImages.clicks().map{PrintToPdfEvents.DeleteAll},
            binding.ivPrintImages.clicks().map{PrintToPdfEvents.PrintPhotos},
        event)
    }

    fun showPhotosToPrint(numberOfPhotos:Int){
        binding.tvPrintToPdfTitle.text = "$numberOfPhotos ${ binding.tvPrintToPdfTitle.context.getString(R.string.photos)}"
    }

    fun showArrange(arrange: PdfArrange){
        val i = arranges.indexOf(arrange)
        binding.spinnerImagesPerPage.setSelection(i)
    }
}