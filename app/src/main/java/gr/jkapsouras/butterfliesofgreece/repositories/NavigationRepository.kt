package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange
import io.reactivex.rxjava3.core.Observable

class NavigationRepository(private val storage: Storage) {
    fun selectFamilyId(familyId:Int): Observable<Int> {
       return Observable.just(familyId)
    }

    fun changeViewArrange() : Observable<ViewArrange>{
        storage.changeArrange()
        return Observable.just(storage.currentArrange)
    }

    fun setViewArrange(arrange:ViewArrange) : Observable<Boolean>{
        return storage.setViewArrange(arrange)
    }

    fun getViewArrange() : Observable<ViewArrange>{
        return Observable.just(storage.currentArrange)
    }
}