package gr.jkapsouras.butterfliesofgreece.families.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange

sealed class FamiliesViewViewStates(isTransition:Boolean) : ViewState(isTransition) {
    class SwitchViewStyle(val currentArrange: ViewArrange) : FamiliesViewViewStates(false)
    object ToSpecies : FamiliesViewViewStates(true)
    class ShowFamilies(val families:List<Family>) : FamiliesViewViewStates(false)
}