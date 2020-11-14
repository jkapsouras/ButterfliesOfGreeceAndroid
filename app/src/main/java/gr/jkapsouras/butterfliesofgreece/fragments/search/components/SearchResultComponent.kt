package gr.jkapsouras.butterfliesofgreece.fragments.search.components

import android.util.Log
import android.view.View
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.search.viewStates.SearchViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.PhotosTableView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class SearchResultComponent(private val photosTableView: PhotosTableView) : UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosTableView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
        if (viewState is SearchViewStates){
        when (viewState) {
            is SearchViewStates.ShowResult -> {
                Log.println(Log.DEBUG,"","number of species: ${viewState.result.count()}")
                photosTableView.visibility = View.VISIBLE
                photosTableView.showSpecies(
                    species = viewState.result,
                    fromSearch = viewState.fromSearch
                )
            }
            else ->
            print("default state")
        }
    }
}
}