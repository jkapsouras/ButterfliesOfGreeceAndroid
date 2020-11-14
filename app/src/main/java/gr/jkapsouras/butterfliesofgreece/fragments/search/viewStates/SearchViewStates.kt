package gr.jkapsouras.butterfliesofgreece.fragments.search.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.dto.Specie

sealed class SearchViewStates(isTransition:Boolean): ViewState(isTransition) {
    class ShowResult(val result:List<Specie>, val fromSearch: Boolean) : SearchViewStates(false)
    object ToPhotosOfSpecie: SearchViewStates(true)
}