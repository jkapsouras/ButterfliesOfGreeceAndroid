package gr.jkapsouras.butterfliesofgreece.fragments.families.photos.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class PhotosEvents: UiEvent() {
    class LoadPhotos(val specieId:Int):PhotosEvents()
    class PhotoClicked(val id:Int):PhotosEvents()
    class AddPhotoForPrintClicked(val photoId:Int):PhotosEvents()
}