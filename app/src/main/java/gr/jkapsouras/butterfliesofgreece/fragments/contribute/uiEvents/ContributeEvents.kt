package gr.jkapsouras.butterfliesofgreece.fragments.contribute.uiEvents

import android.location.Location
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import java.time.LocalDate
import java.util.*

sealed class ContributeEvents: UiEvent() {
    object TextDateClicked : ContributeEvents()
    class ButtonDoneClicked(val date: LocalDate): ContributeEvents()
    class LocationFetched(val location: Location): ContributeEvents()
    class TextNameSet(val name:String): ContributeEvents()
    class TextAltitudeSet(val altitude:String): ContributeEvents()
    class TextPlaceSet(val place:String): ContributeEvents()
    class TextStateSet(val stage:String): ContributeEvents()
    class TextLatitudeSet(val latitude:String): ContributeEvents()
    class TextLongitudeSet(val longitude:String): ContributeEvents()
    class TextGenusSpeciesSet(val genusSpecies:String): ContributeEvents()
    class TextNameSpeciesSet(val nameSpecies:String): ContributeEvents()
    class TextCommentsSet(val comments:String): ContributeEvents()
    object AddClicked: ContributeEvents()
    object ExportClicked: ContributeEvents()
    object SharePdf: ContributeEvents()
    object InstructionsClicked: ContributeEvents()
    object ClosePdf: ContributeEvents()
}