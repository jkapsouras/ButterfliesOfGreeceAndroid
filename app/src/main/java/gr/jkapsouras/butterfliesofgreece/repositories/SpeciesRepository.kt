package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import io.reactivex.rxjava3.core.Observable

class SpeciesRepository(private val storage: Storage) {

    fun getAllSpecies():List<Specie>{
        return storage.getAllSpecies()
    }

    fun getSpeciesOfFamily(familyId:Int) : Observable<List<Specie>> {
        return Observable.just(storage.species(familyId))
    }

    fun getSelectedFamilyName(familyId:Int) : Observable<String>{
        return Observable.just(storage.getSelectedFamilyName(familyId))
    }

    fun getSpeciesFromSearchTerm(term:String) : Observable<List<Specie>>{
        return Observable.just(storage.searchSpeciesBy(term))
    }
}