package gr.jkapsouras.butterfliesofgreece.species.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class SpeciesEvents: UiEvent() {
    class LoadSpecies(familyId:Int) : SpeciesEvents()
    class SpecieClicked(id:Int) : SpeciesEvents()
    class AddPhotosForPrintClicked(specieId:Int) : SpeciesEvents()
}
