package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.dto.CatFact
import gr.jkapsouras.butterfliesofgreece.network.IImageApi
import io.reactivex.rxjava3.core.Observable

class RecognitionRepository(private val api:IImageApi) {

    fun test() : Observable<CatFact> {
        return api.getMovies()
    }

//    fun recognize(image:Avatar) -> Observable<Predictions>{
//        return api.Analyze(image: image)
//    }
}