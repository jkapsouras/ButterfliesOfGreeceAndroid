package gr.jkapsouras.butterfliesofgreece.dto

data class Prediction(
    val butterflyClass:String = "",
    val output:Double = 0.0,
    val prob:Double = 0.0) {
}