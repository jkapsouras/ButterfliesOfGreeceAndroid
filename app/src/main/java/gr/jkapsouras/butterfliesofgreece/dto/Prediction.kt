package gr.jkapsouras.butterfliesofgreece.dto

import com.google.gson.annotations.SerializedName

data class Predictions(val predictions:List<Prediction>) {
}

data class Prediction(
    @SerializedName("class")
    val butterflyClass:String = "",
    val output:Double = 0.0,
    val prob:Double = 0.0) {
}