package gr.jkapsouras.butterfliesofgreece.fragments.species.state

import gr.jkapsouras.butterfliesofgreece.dto.Specie

class SpeciesState(val species: List<Specie>)

fun SpeciesState.with(species: List<Specie>? = null): SpeciesState {
    return SpeciesState(species ?: this.species)
}