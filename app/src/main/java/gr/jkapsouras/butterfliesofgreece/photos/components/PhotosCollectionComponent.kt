package gr.jkapsouras.butterfliesofgreece.photos.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.photos.viewStates.PhotosViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosCollectionView.PhotosCollectionView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class PhotosCollectionComponent(private val photosCollectionView: PhotosCollectionView) :
    UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosCollectionView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
        if (viewState is PhotosViewStates){
            when (viewState) {
                is PhotosViewStates.ShowPhotos -> {
                    print("number of families: ${viewState.photos}")
                    photosCollectionView.showPhotos(viewState.photos)
                }
                is PhotosViewStates.SwitchViewStyle -> {
                    if (viewState.currentArrange == ViewArrange.Grid) {
                        photosCollectionView.show()
                    } else {
                        photosCollectionView.hide()
                    }
                }
            }
        }
    }
}