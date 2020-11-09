package gr.jkapsouras.butterfliesofgreece.fragments.search.components

import androidx.appcompat.widget.SearchView
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.search.uiEvents.SearchEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class SearchHeaderComponent(searchView: SearchView) : UiComponent {
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(qString: String): Boolean {
                event.onNext(SearchEvents.SearchWith(qString))
                return true
            }
            override fun onQueryTextSubmit(qString: String): Boolean {
                event.onNext(SearchEvents.SearchWith(qString))
                return true
            }
        })

        uiEvents = event
    }

    override fun renderViewState(viewState: ViewState)
    {
    }
}
