package gr.jkapsouras.butterfliesofgreece.dto

import com.google.gson.annotations.SerializedName

enum class Genre{
    M,
    F
}

data class ButterflyPhoto(val id:Int,
                          @SerializedName("src")
                          val source:String,
                          val title:String,
                          val author:String,
                          val genre:Genre,
                          @SerializedName("iden")
                          var identified:Boolean,
                          var familyId:Int = -1,
                          var specieId:Int = -1,
                          var specieName:String = ""
)