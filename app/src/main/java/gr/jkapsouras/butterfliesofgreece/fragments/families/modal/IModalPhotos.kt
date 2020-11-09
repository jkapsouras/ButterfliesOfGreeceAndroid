package gr.jkapsouras.butterfliesofgreece.fragments.families.modal

import gr.jkapsouras.butterfliesofgreece.fragments.families.modal.viewStates.ModalViewStates

interface IModalPhotos {
    fun setUpPagesStartingWith(index: Int, photos: List<String>)
}