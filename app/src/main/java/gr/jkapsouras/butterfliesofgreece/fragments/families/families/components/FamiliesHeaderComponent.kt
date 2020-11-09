package gr.jkapsouras.butterfliesofgreece.fragments.families.families.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.viewStates.FamiliesViewViewStates
import gr.jkapsouras.butterfliesofgreece.views.header.HeaderView
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.HeaderViewViewStates
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class HeaderTableComponent(private val headerView: HeaderView) : UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(headerView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
        if (viewState is FamiliesViewViewStates){
            when (viewState) {
                is FamiliesViewViewStates.SwitchViewStyle -> {
                    headerView.changeViewForViewArrange(viewState.currentArrange)
                }
            }
        }
        if (viewState is HeaderViewViewStates){
            when (viewState) {
                is HeaderViewViewStates.UpdateFolderIcon -> {
                    headerView.updateNumberOfPhotos(viewState.numberOfPhotos)
                }
                is HeaderViewViewStates.SetHeaderTitle -> {
                    headerView.updateTitle(viewState.headerTitle)
                }
            }
        }
    }
}