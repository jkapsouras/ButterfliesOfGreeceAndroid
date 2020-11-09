package gr.jkapsouras.butterfliesofgreece.fragments.families.previewer.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class PdfPreviewEvents: UiEvent() {
    object SharePdf : PdfPreviewEvents()
    object CreatePdf : PdfPreviewEvents()
}
