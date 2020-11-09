package gr.jkapsouras.butterfliesofgreece.fragments.families.families.uiEvents

import gr.jkapsouras.butterfliesofgreece.base.UiEvent

sealed class FamilyEvents : UiEvent() {
    object LoadFamilies : FamilyEvents()
    class FamilyClicked(val id: Int) : FamilyEvents()
    class AddPhotosForPrintClicked(val familyId:Int) : FamilyEvents()
}