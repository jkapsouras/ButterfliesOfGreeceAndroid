package gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PdfArrange

sealed class PrintToPdfEvents: UiEvent() {
    object LoadPhotos : PrintToPdfEvents()
    object ChangeArrangeClicked : PrintToPdfEvents()
    class ArrangeSelected(val pdfArrange: PdfArrange) : PrintToPdfEvents()
    object DeleteAll : PrintToPdfEvents()
    class Delete(val photo: ButterflyPhoto) : PrintToPdfEvents()
    object PrintPhotos : PrintToPdfEvents()
}