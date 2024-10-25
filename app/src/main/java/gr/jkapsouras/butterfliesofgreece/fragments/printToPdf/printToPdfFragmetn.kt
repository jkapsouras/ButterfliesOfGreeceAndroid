package gr.jkapsouras.butterfliesofgreece.fragments.printToPdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.PrintToPdfFragmentBinding
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.components.PhotosToPrintComponent
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.components.PhotosToPrintHeaderComponent
import org.koin.android.ext.android.inject

class PrintToPdfFragment : BaseFragment<PrintToPdfPresenter>() {
    private var _binding: PrintToPdfFragmentBinding? = null
    private val binding get() = _binding!!
    override val presenter: PrintToPdfPresenter by inject()
    private var photosToPrintComponent: PhotosToPrintComponent? = null
    private var photosToPrintHeaderComponent: PhotosToPrintHeaderComponent? = null

    override val layoutResource: Int
        get() = R.layout.print_to_pdf_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PrintToPdfFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        photosToPrintComponent = PhotosToPrintComponent(binding.viewTablePhotosToPrint)
        photosToPrintHeaderComponent = PhotosToPrintHeaderComponent(binding.viewHeaderPrintToPdf)
        return listOf(photosToPrintComponent!!, photosToPrintHeaderComponent!!)
    }

}