package gr.jkapsouras.butterfliesofgreece.fragments.printToPdf

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.components.PhotosToPrintComponent
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.components.PhotosToPrintHeaderComponent
import kotlinx.android.synthetic.main.print_to_pdf_fragment.*
import org.koin.android.ext.android.inject

class PrintToPdfFragment : BaseFragment<PrintToPdfPresenter>() {

    override val presenter: PrintToPdfPresenter by inject()
    private var photosToPrintComponent: PhotosToPrintComponent? = null
    private var photosToPrintHeaderComponent: PhotosToPrintHeaderComponent? = null

    override val layoutResource: Int
        get() = R.layout.print_to_pdf_fragment

    override fun initView(view: View): View {
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        photosToPrintComponent = PhotosToPrintComponent(view_table_photos_to_print)
        photosToPrintHeaderComponent = PhotosToPrintHeaderComponent(view_header_print_to_pdf)
        return listOf(photosToPrintComponent!!, photosToPrintHeaderComponent!!)
    }

}