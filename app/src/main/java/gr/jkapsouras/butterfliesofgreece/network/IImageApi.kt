package gr.jkapsouras.butterfliesofgreece.network

import gr.jkapsouras.butterfliesofgreece.dto.CatFact
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface IImageApi {

    @GET("/facts/random")
    fun getMovies(): Observable<CatFact>

}