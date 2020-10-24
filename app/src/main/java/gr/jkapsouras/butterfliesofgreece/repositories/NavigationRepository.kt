package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import io.reactivex.rxjava3.core.Observable

class NavigationRepository(private val storage: Storage) {
    fun selectFamilyId(familyId:Int): Observable<Int> {
        return Observable.fromPublisher { 1 }
    }
}