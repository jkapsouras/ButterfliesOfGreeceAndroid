package gr.jkapsouras.butterfliesofgreece.fragments.legal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.LegalFragmentBinding
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.legal.components.LegalComponent
import org.koin.android.ext.android.inject

class LegalFragment : BaseFragment<LegalPresenter>() {
    private var _binding: LegalFragmentBinding? = null
    private val binding get() = _binding!!
    override val presenter: LegalPresenter by inject()
    private var legalComponent: LegalComponent? = null

    override val layoutResource: Int
        get() = R.layout.legal_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LegalFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        legalComponent = LegalComponent(
            pdfView = binding.legalPdfView,
            okButton = binding.buttonLegalOk,
            termsButton = binding.buttonTermsLegal,
            formButton = binding.buttonFormLegal,
            viewPopup = binding.viewPopup,
            messageLabel = binding.labelLegalPopupMsg
        )
        return listOf(legalComponent!!)
    }
}