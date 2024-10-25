package gr.jkapsouras.butterfliesofgreece.fragments.previewer.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class PdfPreviewEvents: UiEvent() {
    data object SharePdf : PdfPreviewEvents()
    data object CreatePdf : PdfPreviewEvents()
    data object DeleteData : PdfPreviewEvents()
}
