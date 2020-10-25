package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.dto.Family
import io.reactivex.rxjava3.core.Observable

class FamiliesRepository(private val storage:Storage) {
    fun getAllFamilies() : Observable<List<Family>> {
        return Observable.just(storage.families)
    }
}
