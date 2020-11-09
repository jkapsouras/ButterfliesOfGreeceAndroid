package gr.jkapsouras.butterfliesofgreece.fragments.families.state

import gr.jkapsouras.butterfliesofgreece.dto.Family

class FamiliesState(val families: List<Family>){

}

fun FamiliesState.with(families: List<Family>? = null): FamiliesState {
    return FamiliesState(families ?: this.families)
}