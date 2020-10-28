package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.ViewArrange
import io.reactivex.rxjava3.core.Observable

class NavigationRepository(private val storage: Storage) {
    fun selectFamilyId(familyId:Int): Observable<Boolean> {
       return storage.setFamilyId(familyId)
    }

    fun getFamilyId() : Observable<Int>{
        return storage.getFamilyId()
    }

    fun changeViewArrange() : Observable<ViewArrange>{
        storage.changeArrange()
        return Observable.just(storage.currentArrange)
    }

    fun setViewArrange(arrange: ViewArrange) : Observable<Boolean>{
        return storage.setViewArrange(arrange)
    }

    fun getViewArrange() : Observable<ViewArrange>{
        return Observable.just(storage.currentArrange)
    }

    fun selectSpecieId(specieId:Int) : Observable<Boolean>{
        return storage.setSpecieId(specieId)
    }

    fun getSpecieId() : Observable<Int>{
        return storage.getSpecieId()
    }

     fun selectPhotoId(photoId:Int) : Observable<Boolean>{
        return storage.setPhotoId(photoId)
    }
}