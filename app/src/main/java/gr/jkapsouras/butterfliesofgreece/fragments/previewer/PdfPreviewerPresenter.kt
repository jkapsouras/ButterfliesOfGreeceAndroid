package gr.jkapsouras.butterfliesofgreece.fragments.previewer

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.View
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.state.PdfPreviewerState
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.uiEvents.PdfPreviewEvents
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.viewStates.PdfPreviewViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PdfArrange
import gr.jkapsouras.butterfliesofgreece.managers.PdfManager
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosToPrintRepository
import io.reactivex.rxjava3.core.Observable

class PdfPreviewPresenter(
    private val photosToPrintRepository: PhotosToPrintRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler) {

    var pdfState: PdfPreviewerState = PdfPreviewerState("", emptyList(), PdfArrange.OnePerPage)
    private val pdfCreator: PdfManager = PdfManager()
    private lateinit var view: View
    private lateinit var context: Context

    override fun setupEvents() {
        Observable.zip(photosToPrintRepository.getPdfArrange(),
            photosToPrintRepository.getPhotosToPrint(),
            { pdfArrange, photos -> Pair(pdfArrange, photos) })
            .subscribeOn(backgroundThreadScheduler.scheduler)
            .map { data ->
                pdfState = if (data.second.isEmpty())
                    PdfPreviewerState("", emptyList(), PdfArrange.OnePerPage)
                else
                    pdfState.with(
                        pdfData = pdfCreator.createPdf(
                            view = view,
                            context = context,
                            photos = data.second,
                            pdfArrange = data.first
                        ), photos = data.second, pdfArrange = data.first
                    )
                pdfState
            }
            .subscribe { data ->
                if(data.pdfData.isEmpty())
                    state.onNext(PdfPreviewViewStates.ToMainMenu)
                else
                    state.onNext(PdfPreviewViewStates.ShowPdf(pdfData = pdfState.pdfData))
            }
            .disposeWith(disposables)
    }

    fun setView(view: View) {
        this.view = view
    }

    fun setContext(context: Context) {
        this.context = context
    }

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is PdfPreviewEvents -> {
                when (uiEvent) {
                    is PdfPreviewEvents.SharePdf ->
                        state.onNext(PdfPreviewViewStates.ShowShareDialog(pdfData = pdfState.pdfData))
                    is PdfPreviewEvents.DeleteData ->
                        photosToPrintRepository.deleteAll()
                            .subscribe()
                            .disposeWith(disposables)
                    else ->
                        Log.d(ContentValues.TAG, "nothing")
                }
            }
            else ->
                state.onNext(GeneralViewState.Idle)
        }
    }
}
