package gr.jkapsouras.butterfliesofgreece.fragments.legal.components

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.github.barteksc.pdfviewer.PDFView
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.extensions.ClickSpan
import gr.jkapsouras.butterfliesofgreece.fragments.legal.uiEvents.LegalEvents
import gr.jkapsouras.butterfliesofgreece.fragments.legal.viewStates.LegalViewStates
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class LegalComponent(val pdfView: PDFView, val okButton: Button, val termsButton: Button,
                     val formButton: Button, val viewPopup: View, val messageLabel: TextView
) : UiComponent {

    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {

        okButton.setOnClickListener {
            event.onNext(LegalEvents.OkClicked)
        }

        termsButton.setOnClickListener {
            event.onNext(LegalEvents.TermsClicked)
        }

        formButton.setOnClickListener {
            event.onNext(LegalEvents.FormsClicked)
        }

        ClickSpan.clickify(messageLabel, messageLabel.context.getString(R.string.legal_link_first)) {
            val url = "https://yperdiavgeia.gr/decisions/downloadPdf/15680414"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            messageLabel.context.startActivity(intent)
        }

        ClickSpan.clickify(messageLabel, messageLabel.context.getString(R.string.legal_link_second)) {
            val url = "http://www.ypeka.gr/Default.aspx?tabid=918&language=en-US"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            messageLabel.context.startActivity(intent)
        }

        ClickSpan.clickify(messageLabel, messageLabel.context.getString(R.string.legal_link_third)) {
            val url = "http://www.ypeka.gr/LinkClick.aspx?fileticket=FncrrCKwYAE%3D&tabid=918&language=el-GR"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            messageLabel.context.startActivity(intent)
        }

        uiEvents = event
    }

    override fun renderViewState(viewState: ViewState) {
        if (viewState is LegalViewStates) {
            when (viewState) {
                is LegalViewStates.ShowTermsPdf -> {
                    var stream = termsButton.context?.assets?.open(viewState.document)
                    pdfView.fromStream(stream!!)
                        .password(null) // if password protected, then write password
                        .defaultPage(0) // set the default page to open
                        .enableSwipe(true)
                        .spacing(8)
                        .load()
                    termsButton.setTextColor(termsButton.context.getColor(R.color.legal))
                    formButton.setTextColor(termsButton.context.getColor(R.color.gray))
                }
                is LegalViewStates.ShowFormsPdf -> {
                    var stream = termsButton.context?.assets?.open(viewState.document)
                    pdfView.fromStream(stream!!)
                        .password(null) // if password protected, then write password
                        .defaultPage(0) // set the default page to open
                        .enableSwipe(true)
                        .spacing(8)
                        .load()
                    formButton.setTextColor(termsButton.context.getColor(R.color.legal))
                    termsButton.setTextColor(termsButton.context.getColor(R.color.gray))
                }
                LegalViewStates.ShowPopup ->
                    viewPopup.visibility = View.VISIBLE
                LegalViewStates.HidePopup ->
                    viewPopup.visibility = View.GONE
            }
        }
    }
}
