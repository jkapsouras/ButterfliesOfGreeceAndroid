package gr.jkapsouras.butterfliesofgreece.fragments.families.search.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.Specie

sealed class SearchEvents: UiEvent() {
    class SearchWith(val term:String) : SearchEvents()
    class SpecieClicked(val specie: Specie) :SearchEvents()
}
