package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.managers.ICacheManager
import io.reactivex.rxjava3.core.Observable

class PhotosToPrintRepository(private val storage: Storage,
private val cacheManager: ICacheManager){

    fun getPhotosToPrint() : Observable<List<ButterflyPhoto>>{
        return cacheManager.getPhotosToPrint()
    }

    fun savePhotosToPrint(photos:List<ButterflyPhoto>) : Observable<List<ButterflyPhoto>>{
        return cacheManager.savePhotosToPrint(photos).map{ photos }
    }

    fun delete(photo:ButterflyPhoto) : Observable<List<ButterflyPhoto>>{
        return cacheManager.delete(photo)
    }

    fun deleteAll() : Observable<Boolean>{
        return cacheManager.clear()
    }

//    func getPdfArrange() -> Observable<PdfArrange>{
//        return Observable.from(optional: storage.getPdfArrange())
//    }
//
//    func setPdfArrange(pdfArrange:PdfArrange) -> Observable<Bool>{
//        return storage.setPdfArrange(pdfArrange: pdfArrange)
//    }

}