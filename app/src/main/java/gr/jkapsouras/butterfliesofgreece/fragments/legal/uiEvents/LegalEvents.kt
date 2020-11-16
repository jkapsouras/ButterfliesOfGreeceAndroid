package gr.jkapsouras.butterfliesofgreece.fragments.legal.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class LegalEvents: UiEvent(){
    object TermsClicked : LegalEvents()
    object FormsClicked : LegalEvents()
    object OkClicked : LegalEvents()
}