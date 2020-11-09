package gr.jkapsouras.butterfliesofgreece.fragments.families.previewer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Genre
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.FamiliesPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.components.FamiliesCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.components.FamiliesTableComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.components.HeaderTableComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.previewer.component.PdfPreviewComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.printToPdf.state.PdfArrange
import gr.jkapsouras.butterfliesofgreece.managers.PdfManager
import kotlinx.android.synthetic.main.families_fragment.*
import kotlinx.android.synthetic.main.pdf_previewer_fragment.*
import org.koin.android.ext.android.inject

class PdfPreviewerFragment : BaseFragment<PdfPreviewPresenter>() {

    override val presenter: PdfPreviewPresenter by inject()
    private var pdfPreviewComponent: PdfPreviewComponent? = null

    override val layoutResource: Int
        get() = R.layout.pdf_previewer_fragment

    override fun initView(view: View): View {
        presenter.setView(view)
        presenter.setContext(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        pdfPreviewComponent = PdfPreviewComponent(requireActivity(), previewer_pdfView, button_share, previewer_progressBar)
        return listOf(pdfPreviewComponent!!)
    }
}