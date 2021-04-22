package gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state

import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto

enum class PdfArrange(private val screenName: String){
    OnePerPage("1"),
    TwoPerPage("2"),
    FourPerPage("4"),
    SixPerPage("6");

    override fun toString(): String {
        return screenName
    }
}

class PhotosToPdfState(val photos: List<ButterflyPhoto>, val pdfArrange: PdfArrange)

fun PhotosToPdfState.with(photos: List<ButterflyPhoto>? = null, pdfArrange:PdfArrange? = null) : PhotosToPdfState {
        return PhotosToPdfState(
            photos = photos ?: this.photos,
            pdfArrange = pdfArrange ?: this.pdfArrange
        )
    }
