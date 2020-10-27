package gr.jkapsouras.butterfliesofgreece.species.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange

sealed class SpeciesViewStates(isTransition:Boolean) : ViewState(isTransition) {
    class SwitchViewStyle(val currentArrange: ViewArrange) : SpeciesViewStates(false)
    object ToPhotos : SpeciesViewStates(true)
    class ShowSpecies(val species:List<Specie>, val fromSearch: Boolean) : SpeciesViewStates(false)
}