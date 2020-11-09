package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import io.reactivex.rxjava3.core.Observable

class PhotosRepository(private val storage: Storage) {

     fun getPhotosOfSpecie(specieId:Int) : Observable<List<ButterflyPhoto>>{
        return Observable.just( storage.photos(specieId))
    }

    fun getSelectedSpecieName(specieId:Int) : Observable<String>{
        return Observable.just(storage.getSelectedSpecieName( specieId))
    }
}
