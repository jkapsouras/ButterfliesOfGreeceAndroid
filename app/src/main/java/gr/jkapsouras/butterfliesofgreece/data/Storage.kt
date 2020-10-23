package gr.jkapsouras.butterfliesofgreece.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import java.io.IOException

class Storage(context: Context) {
    private val families:List<Family>

    init {
        val jsonData = readData(context, "data.json")
        families = if(jsonData == null)
            emptyList()
        else {
            val sType = object : TypeToken<List<Family>>() {}.type
            val tmpFamilies = Gson().fromJson<List<Family>>(jsonData, sType)
            tmpFamilies.map { family ->
                Family(family.id, family.name,family.photo, family.species.map { specie ->
                    Specie(specie.id, specie.name, specie.imageTitle, specie.photos.map { photo->
                        ButterflyPhoto(photo.id, photo.source, photo.title, photo.author, photo.genre, photo.identified, family.id, specie.id, specie.name)
                    }, family.id)
                })
            }
        }
    }

    fun print(){
        print(families[0].name)
    }
}

fun readData(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}