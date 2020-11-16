package gr.jkapsouras.butterfliesofgreece.fragments.legal

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.legal.components.LegalComponent
import kotlinx.android.synthetic.main.legal_fragment.*
import org.koin.android.ext.android.inject

class LegalFragment : BaseFragment<LegalPresenter>() {

    override val presenter: LegalPresenter by inject()
    private var legalComponent: LegalComponent? = null

    override val layoutResource: Int
        get() = R.layout.legal_fragment

    override fun initView(view: View): View {
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        legalComponent = LegalComponent(pdfView = legal_pdf_view, okButton = button_legal_ok, termsButton = button_terms_legal, formButton = button_form_legal, viewPopup = view_popup, messageLabel = label_legal_popup_msg)
        return listOf(legalComponent!!)
    }
}