package gr.jkapsouras.butterfliesofgreece.fragments.families.previewer

import android.content.Context
import android.view.View
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.families.previewer.state.PdfPreviewerState
import gr.jkapsouras.butterfliesofgreece.fragments.families.previewer.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.families.previewer.uiEvents.PdfPreviewEvents
import gr.jkapsouras.butterfliesofgreece.fragments.families.previewer.viewStates.PdfPreviewViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.families.printToPdf.state.PdfArrange
import gr.jkapsouras.butterfliesofgreece.managers.PdfManager
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosToPrintRepository
import io.reactivex.rxjava3.core.Observable
import java.io.File

class PdfPreviewPresenter(
    private val photosToPrintRepository: PhotosToPrintRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler) {

    var pdfState: PdfPreviewerState = PdfPreviewerState("", emptyList(), PdfArrange.OnePerPage)
    var pdfCreator: PdfManager = PdfManager()
    private lateinit var view: View
    private lateinit var context:Context

    override fun setupEvents() {
        Observable.zip(photosToPrintRepository.getPdfArrange(), photosToPrintRepository.getPhotosToPrint(),
            {pdfArrange, photos -> Pair(pdfArrange, photos)})
            .subscribeOn(backgroundThreadScheduler.scheduler)
            .map{data ->
                pdfState = pdfState.with(pdfData = pdfCreator.createPdf(view = view, context = context, photos = data.second, pdfArrange = data.first), photos = data.second, pdfArrange = data.first)
                pdfState
            }
            .subscribe{data ->
                    state.onNext(PdfPreviewViewStates.ShowPdf(pdfData = pdfState.pdfData))
            }
            .disposeWith(disposables)
    }

    fun setView(view: View)
    {
        this.view = view
    }

    fun setContext(context: Context)
    {
        this.context=context
    }

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is PdfPreviewEvents -> {
                when (uiEvent) {
                    is PdfPreviewEvents.SharePdf ->
                    state.onNext(PdfPreviewViewStates.ShowShareDialog(pdfData = pdfState. pdfData))
                }
            }
            else ->
            state.onNext(GeneralViewState.Idle)
        }
    }
}
