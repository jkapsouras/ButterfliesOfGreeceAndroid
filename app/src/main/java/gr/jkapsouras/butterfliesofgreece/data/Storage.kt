package gr.jkapsouras.butterfliesofgreece.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.families.ViewArrange
import io.reactivex.rxjava3.core.Observable
import java.io.IOException
import java.util.*

class Storage(context: Context) {
    val families:List<Family>
    var currentArrange: ViewArrange = ViewArrange.Grid
    var familyId: Int = -1
    var specieId:Int = -1
    var photoId:Int = -1
//    static var pdfArrange:PdfArrange?

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

    fun species(familyId:Int) : List<Specie>{
        return families.filter{it.id == familyId}.flatMap{it.species}
    }

//    func getPdfArrange() -> PdfArrange{
//        return Storage.pdfArrange ?? PdfArrange.onePerPage
//    }

//    func setPdfArrange(pdfArrange:PdfArrange) -> Observable<Bool> {
//        return Observable.from(optional: Storage.pdfArrange = pdfArrange).map({ _ in return true})
//    }

    fun photos(specieId:Int) : List<ButterflyPhoto>{
        return species(familyId).filter{it.id == specieId}.flatMap{it.photos}
    }

     fun getSelectedFamilyName(familyId:Int) : String{
        return families.first{it.id == familyId}.name
    }

    fun getSelectedSpecieName(specieId:Int) : String{
        return families.first{it.id == familyId}.species.first{it.id == specieId}.name
    }

    fun setFamilyId(familyId:Int) : Observable<Boolean>{
        return Observable.just(familyId).map{ this.familyId = it }.map{true}
    }

    fun getFamilyId() : Observable<Int>{
        return Observable.just(familyId)
    }

    fun setSpecieId(specieId:Int) : Observable<Boolean>{
        return Observable.just(specieId).map{this.specieId = it}.map{true}
    }

    fun getSpecieId() : Observable<Int>{
        return Observable.just(specieId)
    }

    fun setPhotoId(photoId:Int) : Observable<Boolean>{
        return Observable.just(photoId).map{this.photoId = it}.map{true}
    }

    fun getPhotoId() : Observable<Int>{
        return Observable.just(photoId)
    }

    fun setViewArrange(currentArrange:ViewArrange) : Observable<Boolean>{
        return Observable.just(currentArrange).map{this.currentArrange = it}.map{true}
    }

     fun changeArrange(){
        currentArrange = ViewArrange.changeArrange(currentArrange)
    }

     fun getAllPhotos() : Observable<List<ButterflyPhoto>>{
        return Observable.just(families.flatMap{it.species}.flatMap{it.photos})
    }

    fun getAllSpecies() : List<Specie>{
        return families.flatMap{it.species}
    }

    fun searchSpeciesBy(term:String) : List<Specie>{
        return getAllSpecies().filter{specie ->
                specie.name.toLowerCase(Locale.ROOT).contains(term.toLowerCase(Locale.ROOT))}
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