package gr.jkapsouras.butterfliesofgreece.network

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface IImageApi {

    @GET("/3/movie/popular")
    fun getMovies(): Observable<String>

}