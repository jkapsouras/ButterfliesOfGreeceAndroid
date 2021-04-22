package gr.jkapsouras.butterfliesofgreece.fragments.modal.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState

sealed class ModalViewStates(isTransition:Boolean): ViewState(isTransition) {
    class ShowPhotosStartingWith(val index: Int, val photos: List<String>) : ModalViewStates(false)
    object CloseModal : ModalViewStates(true)
}