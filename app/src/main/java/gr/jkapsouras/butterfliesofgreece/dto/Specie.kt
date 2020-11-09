package gr.jkapsouras.butterfliesofgreece.dto

import com.google.gson.annotations.SerializedName

data class Specie (val id:Int,
                   val name:String,
                   @SerializedName("imgtitle")
                   val imageTitle: String,
                   val photos:List<ButterflyPhoto>,
                   var familyId: Int = -1)
{

}