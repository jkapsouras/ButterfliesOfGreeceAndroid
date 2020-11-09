package gr.jkapsouras.butterfliesofgreece.fragments.families.modal.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class ModalEvents: UiEvent() {
    class LoadPhotos(val specieId:Int,val photoId:Int) : ModalEvents()
    object CloseModalClicked : ModalEvents()
}