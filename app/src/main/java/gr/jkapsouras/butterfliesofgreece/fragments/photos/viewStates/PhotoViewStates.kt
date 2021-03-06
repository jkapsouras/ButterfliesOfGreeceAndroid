package gr.jkapsouras.butterfliesofgreece.fragments.photos.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.families.ViewArrange

sealed class PhotosViewStates(isTransition:Boolean): ViewState(isTransition) {
    class SwitchViewStyle(val currentArrange: ViewArrange) : PhotosViewStates(false)
    class ToPhoto(val photoId: Int) : PhotosViewStates(true)
    class ShowPhotos(val photos: List<ButterflyPhoto>) : PhotosViewStates(false)
}