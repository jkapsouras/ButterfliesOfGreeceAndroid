package gr.jkapsouras.butterfliesofgreece.fragments.search

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.search.state.SearchState
import gr.jkapsouras.butterfliesofgreece.fragments.search.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.search.uiEvents.SearchEvents
import gr.jkapsouras.butterfliesofgreece.fragments.search.viewStates.SearchViewStates
import gr.jkapsouras.butterfliesofgreece.repositories.NavigationRepository
import gr.jkapsouras.butterfliesofgreece.repositories.SpeciesRepository

class SearchPresenter(
    private val speciesRepository: SpeciesRepository,
    private val navigationRepository: NavigationRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    private var searchState: SearchState = SearchState(term = "", result = emptyList())

    override fun setupEvents() {
        emitter.onNext(SearchEvents.SearchWith(term = searchState.term))
    }

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is SearchEvents ->
            handleSearchEvents(searchEvent = uiEvent)
            else ->
            state.onNext(GeneralViewState.Idle)
        }
    }

    private fun handleSearchEvents(searchEvent: SearchEvents){
        when (searchEvent) {
            is SearchEvents.SearchWith ->
                speciesRepository
                .getSpeciesFromSearchTerm(term = searchEvent.term)
                .map{result ->
                        searchState = searchState.with(term = searchEvent.term, result = result)
                    result
                }
                .subscribe{result ->
                    state.onNext(SearchViewStates.ShowResult(result = result, fromSearch = true))}
                    .disposeWith(disposables)
            is SearchEvents.SpecieClicked ->
            navigationRepository.selectSpecieId(specieId = searchEvent.specie.id)
                .flatMap{
                navigationRepository.selectFamilyId(familyId = searchEvent.specie.familyId)}
            .subscribe{
                    state.onNext(SearchViewStates.ToPhotosOfSpecie)}
                .disposeWith(disposables)
        }
    }

}