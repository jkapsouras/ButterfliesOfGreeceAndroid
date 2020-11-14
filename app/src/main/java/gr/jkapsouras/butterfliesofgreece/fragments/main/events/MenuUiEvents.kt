package gr.jkapsouras.butterfliesofgreece.fragments.main.events

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class MenuUiEvents : UiEvent() {
    object FieldClicked : MenuUiEvents()
    object IntroductionClicked : MenuUiEvents()
    object LegalClicked : MenuUiEvents()
    object AboutClicked : MenuUiEvents()
    object ContributeClicked : MenuUiEvents()
    object EndangeredSpeciesClicked : MenuUiEvents()
    object OnlineRecognitionClicked : MenuUiEvents()
    object OfflineRecognitionClicked : MenuUiEvents()
    object RecognitionClicked : MenuUiEvents()
}