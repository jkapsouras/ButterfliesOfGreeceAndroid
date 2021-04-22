package gr.jkapsouras.butterfliesofgreece.views.header.viewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState

sealed class HeaderViewViewStates(isTransition:Boolean) : ViewState(isTransition) {
    class UpdateFolderIcon(val numberOfPhotos:Int) : HeaderViewViewStates(false)
    class SetHeaderTitle(val headerTitle:String) : HeaderViewViewStates(false)
    class ToSearch(val from:FromFragment) : HeaderViewViewStates(true)
    class ToPrintPhotos(val from:FromFragment) : HeaderViewViewStates(true)
}

enum class FromFragment{
    Families,
    Species,
    Photos
}