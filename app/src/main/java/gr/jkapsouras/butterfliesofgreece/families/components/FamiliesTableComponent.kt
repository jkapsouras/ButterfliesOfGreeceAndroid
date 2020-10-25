package gr.jkapsouras.butterfliesofgreece.families.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.families.viewStates.FamiliesViewViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.PhotosTableView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class FamiliesTableComponent(private val photosTableView: PhotosTableView) : UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosTableView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
    if (viewState is FamiliesViewViewStates){
        when (viewState) {
            is FamiliesViewViewStates.ShowFamilies -> {
            print("number of families: ${viewState.families.count()}")
            photosTableView.showFamilies(viewState.families)
        }
            is FamiliesViewViewStates.SwitchViewStyle -> {
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