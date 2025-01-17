package gr.jkapsouras.butterfliesofgreece.network

import gr.jkapsouras.butterfliesofgreece.dto.Predictions
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


interface IImageApi {

        @Multipart
        @POST("/analyze")
        fun uploadImage(
            @Part("avatar\"; filename=\"avatar\" ") file: RequestBody
        ): Observable<Predictions>

}