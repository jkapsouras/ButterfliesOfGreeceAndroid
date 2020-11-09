package gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.viewStates.PrintToPdfViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.PhotosTableView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class PhotosToPrintComponent(private val photosTableView: PhotosTableView) : UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosTableView.uiEvents,event)
    }

    override fun renderViewState(viewState: ViewState) {
        if (viewState is PrintToPdfViewStates){
        when (viewState) {
            is PrintToPdfViewStates.ShowPhotos -> {
                print("number of species: ${viewState.photos.count()}")
                photosTableView.showPhotosToPrint(photos = viewState.photos)
            }
        }
    }
}
}