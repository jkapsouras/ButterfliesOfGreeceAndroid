package gr.jkapsouras.butterfliesofgreece.fragments.species.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.fragments.species.viewStates.SpeciesViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosCollectionView.PhotosCollectionView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class SpeciesCollectionComponent(private val photosCollectionView: PhotosCollectionView) :
    UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosCollectionView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
        if (viewState is SpeciesViewStates){
            when (viewState) {
                is SpeciesViewStates.ShowSpecies -> {
                    print("number of families: ${viewState.species.count()}")
                    photosCollectionView.showSpecies(viewState.species)
                }
                is SpeciesViewStates.SwitchViewStyle -> {
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