package gr.jkapsouras.butterfliesofgreece.species.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.species.viewStates.SpeciesViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.PhotosTableView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class SpeciesTableComponent(private val photosTableView: PhotosTableView) : UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosTableView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
        if (viewState is SpeciesViewStates){
            when (viewState) {
                is SpeciesViewStates.ShowSpecies -> {
                    print("number of species: ${viewState.species.count()}")
                    photosTableView.showSpecies(viewState.species, viewState.fromSearch)
                }
                is SpeciesViewStates.SwitchViewStyle -> {
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