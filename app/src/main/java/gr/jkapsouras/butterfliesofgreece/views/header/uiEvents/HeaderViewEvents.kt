package gr.jkapsouras.butterfliesofgreece.views.header.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.ViewArrange

sealed class HeaderViewEvents : UiEvent() {
    class InitState(val currentArrange: ViewArrange) : HeaderViewEvents()
    object SwitchViewStyleClicked : HeaderViewEvents()
    object SearchBarClicked : HeaderViewEvents()
    object PrintPhotosClicked : HeaderViewEvents()
}