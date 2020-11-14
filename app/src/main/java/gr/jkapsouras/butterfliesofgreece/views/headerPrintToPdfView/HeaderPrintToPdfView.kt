package gr.jkapsouras.butterfliesofgreece.views.headerPrintToPdfView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.jakewharton.rxbinding4.view.clicks
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PdfArrange
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.uiEvents.PrintToPdfEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_header_print_to_pdf.view.*

class HeaderPrintToPdfView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

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
        view = LayoutInflater.from(context)
            .inflate(R.layout.view_header_print_to_pdf, this)

        val adapter = ArrayAdapter<PdfArrange>(view.context, R.layout.spinner_text, arranges)
        view.spinner_images_per_page.adapter = adapter
        view.spinner_images_per_page.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                event.onNext(PrintToPdfEvents.ArrangeSelected(arranges[position]))
            }

        }
    }

    private fun viewEvents(): Observable<UiEvent> {
        return Observable.merge(view.iv_delete_images.clicks().map{PrintToPdfEvents.DeleteAll},
            view.iv_print_images.clicks().map{PrintToPdfEvents.PrintPhotos},
        event)
    }

    fun showPhotosToPrint(numberOfPhotos:Int){
        view.tv_print_to_pdf_title.text = "$numberOfPhotos ${tv_print_to_pdf_title.context.getString(R.string.photos)}"
    }

    fun showArrange(arrange: PdfArrange){
        val i = arranges.indexOf(arrange)
        spinner_images_per_page.setSelection(i)
    }
}