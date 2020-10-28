package gr.jkapsouras.butterfliesofgreece.photos.state

import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto

class PhotosState(val photos: List<ButterflyPhoto>, val indexOfSelectedPhoto: Int) {
}

fun PhotosState.with(photos: List<ButterflyPhoto>? = null, photoId: Int? = null): PhotosState {
    val tmpPhotos = photos ?: this.photos
    val index = tmpPhotos.indexOfFirst { it.id == photoId ?: -1 }
    return if (index != -1)
        PhotosState(photos ?: this.photos, index)
    else
        PhotosState(photos ?: this.photos, this.indexOfSelectedPhoto)
}
