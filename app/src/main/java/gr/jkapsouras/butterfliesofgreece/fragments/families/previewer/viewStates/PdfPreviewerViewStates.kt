package gr.jkapsouras.butterfliesofgreece.fragments.families.previewer.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState

sealed class PdfPreviewViewStates(isTransition:Boolean) : ViewState(isTransition) {
    class ShowPdf(val pdfData: String) : PdfPreviewViewStates(false)
    class ShowShareDialog(val pdfData: String) : PdfPreviewViewStates(false)
}