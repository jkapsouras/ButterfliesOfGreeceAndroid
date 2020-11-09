package gr.jkapsouras.butterfliesofgreece.dto

import com.google.gson.annotations.SerializedName

data class Family(val id: Int,
                  val name:String,
                  val photo:String,
                  @SerializedName("spieces")
                  val species:List<Specie>)