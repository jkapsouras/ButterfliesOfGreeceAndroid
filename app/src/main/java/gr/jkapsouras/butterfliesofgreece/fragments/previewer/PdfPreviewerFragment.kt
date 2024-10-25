package gr.jkapsouras.butterfliesofgreece.fragments.previewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.PdfPreviewerFragmentBinding
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.component.PdfPreviewComponent
import org.koin.android.ext.android.inject

class PdfPreviewerFragment : BaseFragment<PdfPreviewPresenter>() {
    private var _binding: PdfPreviewerFragmentBinding? = null
    private val binding get() = _binding!!
    override val presenter: PdfPreviewPresenter by inject()
    private var pdfPreviewComponent: PdfPreviewComponent? = null

    override val layoutResource: Int
        get() = R.layout.pdf_previewer_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PdfPreviewerFragmentBinding.inflate(inflater, container, false)
        presenter.setView(binding.root)
        presenter.setContext(requireContext())

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        pdfPreviewComponent = PdfPreviewComponent(requireActivity(),binding.previewerPdfView,binding.buttonShare,binding.previewerProgressBar)
        return listOf(pdfPreviewComponent!!)
    }
}