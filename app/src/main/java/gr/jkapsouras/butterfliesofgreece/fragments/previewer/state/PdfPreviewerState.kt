package gr.jkapsouras.butterfliesofgreece.fragments.previewer.state

import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PdfArrange

class PdfPreviewerState(val pdfData: String, val photos:List<ButterflyPhoto>, val pdfArrange: PdfArrange) {
}

    fun PdfPreviewerState.with(pdfData: String? = null, photos: List<ButterflyPhoto>? = null, pdfArrange:PdfArrange? = null) : PdfPreviewerState {
        return PdfPreviewerState(
            pdfData = pdfData ?: this.pdfData,
            photos = photos ?: this.photos,
            pdfArrange = pdfArrange ?: this.pdfArrange
        )
    }
