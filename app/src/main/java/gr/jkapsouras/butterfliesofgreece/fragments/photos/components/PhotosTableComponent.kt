package gr.jkapsouras.butterfliesofgreece.fragments.photos.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.fragments.photos.viewStates.PhotosViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.PhotosTableView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class PhotosTableComponent(private val photosTableView: PhotosTableView) : UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosTableView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
        if (viewState is PhotosViewStates){
            when (viewState) {
                is PhotosViewStates.ShowPhotos -> {
                    print("number of species: ${viewState.photos.count()}")
                    photosTableView.showPhotos(viewState.photos)
                }
                is PhotosViewStates.SwitchViewStyle -> {
                    if (viewState.currentArrange == ViewArrange.Grid) {
                        photosTableView.hide()
                    } else {
                        photosTableView.show()
                    }
                }
            }
        }
    }
}