package gr.jkapsouras.butterfliesofgreece.fragments.previewer.component

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.uiEvents.PdfPreviewEvents
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.viewStates.PdfPreviewViewStates
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File


class PdfPreviewComponent(val activity: Activity, val pdfView: PDFView, val button:FloatingActionButton, var progressBar: ProgressBar) : UiComponent {
//    let controller:PdfPreviewViewController
//    let navigationItem:UINavigationItem
//    let loadingView:UIActivityIndicatorView

    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {

        button.setOnClickListener {
            event.onNext(PdfPreviewEvents.SharePdf)
            Log.d(Constraints.TAG, "field clicked")
        }

        uiEvents = event
        progressBar.visibility = View.VISIBLE
        button.visibility = View.GONE
    }

//    let share = UIBarButtonItem(barButtonSystemItem: .action, target: self, action: #selector(shareTapped))
//    self.navigationItem.setRightBarButton(share, animated: true)
//    loadingView.alpha=1


//    @objc func shareTapped(){
//    emitter.onNext(PdfPreviewEvents.sharePdf)


    @SuppressLint("RestrictedApi")
    override fun renderViewState(viewState: ViewState) {
        if (viewState is PdfPreviewViewStates) {
            when (viewState) {
                is PdfPreviewViewStates.ShowPdf -> {
                    val file = File(viewState.pdfData)
                        pdfView.fromFile(file)
                            .password(null) // if password protected, then write password
                            .defaultPage(0) // set the default page to open
                            .enableSwipe(true)
                            .spacing(8)
                            .load()


                    progressBar.visibility = View.GONE
                    pdfView.visibility = View.VISIBLE
                    button.visibility = View.VISIBLE
                }
                is PdfPreviewViewStates.ShowShareDialog -> {
                    val file = File(viewState.pdfData)
                    val bmpUri = FileProvider.getUriForFile(
                        activity,
                        "gr.jkapsouras.butterfliesofgreece.fileprovider",
                        file
                    )

                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                    shareIntent.type = "application/pdf"

                    activity.startActivity(shareIntent)
                }
            }
        }
        if (viewState is GeneralViewState) {
            when (viewState) {
                is GeneralViewState.Idle -> {
                    progressBar.visibility = View.VISIBLE
                    pdfView.visibility = View.GONE
                }
            }
        }
    }
}