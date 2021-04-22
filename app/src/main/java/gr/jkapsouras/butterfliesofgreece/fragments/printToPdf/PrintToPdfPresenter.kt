package gr.jkapsouras.butterfliesofgreece.fragments.printToPdf

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PdfArrange
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PhotosToPdfState
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.uiEvents.PrintToPdfEvents
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.viewStates.PrintToPdfViewStates
import gr.jkapsouras.butterfliesofgreece.repositories.NavigationRepository
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosToPrintRepository

class PrintToPdfPresenter(
    private val photosToPrintRepository: PhotosToPrintRepository,
    private val navigationRepository: NavigationRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler) {

    var photosToPdfState = PhotosToPdfState(
        emptyList(), PdfArrange.OnePerPage)

    override fun setupEvents() {
        emitter.onNext(PrintToPdfEvents.LoadPhotos)
    }

    override fun handleEvent(uiEvents: UiEvent) {
        when (uiEvents) {
            is  PrintToPdfEvents ->
            handlePrintToPdfEvents(printToPdfEvents = uiEvents)
            else ->
            state.onNext(GeneralViewState.Idle)
        }
    }

    private fun handlePrintToPdfEvents(printToPdfEvents: PrintToPdfEvents){
        when (printToPdfEvents) {
            is PrintToPdfEvents.LoadPhotos ->
            photosToPrintRepository.getPhotosToPrint()
                .map{photos ->
                        photosToPdfState = photosToPdfState.with(photos = photos)
                    photosToPdfState}
                .subscribe{
                        state.onNext(PrintToPdfViewStates.ShowPhotos(photos = it.photos))
                    state.onNext(PrintToPdfViewStates.ShowNumberOfPhotos(numberOfPhotos = it.photos.count()))}
                .disposeWith(disposables)
            is PrintToPdfEvents.ChangeArrangeClicked ->
            state.onNext(PrintToPdfViewStates.ShowPickArrangeView(currentArrange = photosToPdfState.pdfArrange))
            is PrintToPdfEvents.ArrangeSelected ->
            photosToPrintRepository.setPdfArrange(pdfArrange = printToPdfEvents.pdfArrange)
                .map{
                        photosToPdfState = photosToPdfState.with(pdfArrange = printToPdfEvents.pdfArrange)
                    photosToPdfState}
                .subscribe{
                        state.onNext(PrintToPdfViewStates.ArrangeViewChanged(currentArrange = it.pdfArrange))}
                .disposeWith(disposables)
            is PrintToPdfEvents.Delete ->
            photosToPrintRepository.delete(photo = printToPdfEvents.photo)
                .map{photos ->
                        photosToPdfState = photosToPdfState.with(photos = photos)
                    photosToPdfState
                }
                .subscribe{
                        state.onNext(PrintToPdfViewStates.ShowPhotos(photos = it.photos))
                    state.onNext(PrintToPdfViewStates.ShowNumberOfPhotos(numberOfPhotos = it.photos.count()))}
                .disposeWith(disposables)
            is PrintToPdfEvents.DeleteAll ->
            photosToPrintRepository.deleteAll()
                .subscribe {
                        state.onNext(PrintToPdfViewStates.AllPhotosDeleted)}
                .disposeWith(disposables)
            is PrintToPdfEvents.PrintPhotos ->
            state.onNext(PrintToPdfViewStates.ToPrintPreview)
        }
    }
}