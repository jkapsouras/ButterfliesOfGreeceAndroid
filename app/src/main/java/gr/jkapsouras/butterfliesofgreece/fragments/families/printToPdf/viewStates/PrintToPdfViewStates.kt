package gr.jkapsouras.butterfliesofgreece.fragments.families.printToPdf.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.families.printToPdf.state.PdfArrange

sealed class PrintToPdfViewStates(isTransition: Boolean): ViewState(isTransition) {
    class ShowPhotos(val photos: List<ButterflyPhoto>) : PrintToPdfViewStates(false)
    class ShowNumberOfPhotos(val numberOfPhotos:Int): PrintToPdfViewStates(false)
    class ShowPickArrangeView(val currentArrange: PdfArrange): PrintToPdfViewStates(false)
    class ArrangeViewChanged(val currentArrange: PdfArrange): PrintToPdfViewStates(false)
    object AllPhotosDeleted: PrintToPdfViewStates(true)
    object ToPrintPreview: PrintToPdfViewStates(true)
}
