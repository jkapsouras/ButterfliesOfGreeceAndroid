package gr.jkapsouras.butterfliesofgreece.fragments.legal.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState

sealed class LegalViewStates(isTransition:Boolean): ViewState(isTransition) {
    class ShowTermsPdf(val document: String) : LegalViewStates(false)
    class ShowFormsPdf(val document: String) : LegalViewStates(false)
    object ShowPopup : LegalViewStates(false)
    object HidePopup : LegalViewStates(false)
}