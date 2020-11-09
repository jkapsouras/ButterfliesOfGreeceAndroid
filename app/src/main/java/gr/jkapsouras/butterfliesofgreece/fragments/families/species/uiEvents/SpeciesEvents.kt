package gr.jkapsouras.butterfliesofgreece.fragments.families.species.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class SpeciesEvents: UiEvent() {
    class LoadSpecies(val familyId:Int) : SpeciesEvents()
    class SpecieClicked(val id:Int) : SpeciesEvents()
    class AddPhotosForPrintClicked(val specieId:Int) : SpeciesEvents()
}
