package gr.jkapsouras.butterfliesofgreece.fragments.contribute.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState

sealed class ContributeViewStates(isTransition : Boolean): ViewState(isTransition) {
    object ShowDatePicker : ContributeViewStates(false)
    object HideDatePicker: ContributeViewStates(false)
    class SetDate(val date:String): ContributeViewStates(false)
    object ShowSettingsDialog: ContributeViewStates(false)
    class ShowLocation(val latitude:String, val longitude:String): ContributeViewStates(false)
    object ShowLocationError: ContributeViewStates(false)
    object ShowItemAdded: ContributeViewStates(false)
    object ShowItemNotAdded: ContributeViewStates(false)
    class ShowExtractedPdf(val pdfData: String): ContributeViewStates(false)
    class ShowShareDialog(val pdfData: String): ContributeViewStates(false)
    object ShowInstructions: ContributeViewStates(true)
    object ClosePdf: ContributeViewStates(false)
}