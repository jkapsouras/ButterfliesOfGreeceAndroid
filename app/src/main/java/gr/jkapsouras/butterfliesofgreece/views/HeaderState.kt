package gr.jkapsouras.butterfliesofgreece.views

import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.extensions.uniques
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange

class HeaderState(val photosToPrint:List<ButterflyPhoto>?,
                  val currentArrange: ViewArrange,
                  val headerName:String){
}

fun HeaderState.with(currentArrange:ViewArrange? = null, photosToPrint:List<ButterflyPhoto>? = null, headerName:String? = null): HeaderState{
    if(photosToPrint == null){
        return HeaderState(this.photosToPrint, currentArrange ?: this.currentArrange, headerName ?: this.headerName)
    }

    if(this.photosToPrint == null){
        return HeaderState(photosToPrint, currentArrange ?: this.currentArrange, headerName ?: this.headerName )
    }

    val tmpPhotos = this.photosToPrint + photosToPrint
    return HeaderState(tmpPhotos.uniques(), currentArrange ?: this.currentArrange, headerName ?: this.headerName)
}
