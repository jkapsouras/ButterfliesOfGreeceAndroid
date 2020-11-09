package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.state.PdfArrange
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

    fun getPdfArrange() : Observable<PdfArrange>{
        return Observable.just(storage.getPdfArrange())
    }

    fun setPdfArrange(pdfArrange:PdfArrange) : Observable<Boolean>{
        return storage.setPdfArrange(pdfArrange = pdfArrange)
    }

}