package gr.jkapsouras.butterfliesofgreece.fragments.previewer

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.component.PdfPreviewComponent
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