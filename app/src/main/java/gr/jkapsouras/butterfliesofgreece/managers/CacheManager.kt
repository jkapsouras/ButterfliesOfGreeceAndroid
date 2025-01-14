package gr.jkapsouras.butterfliesofgreece.managers

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.ContributionItem
import io.reactivex.rxjava3.core.Observable
import java.lang.Exception


interface ICacheManager {

    val photosToPrint:String
    val prefs:SharedPreferences
    val contributionItems:String

    fun  savePhotosToPrint(photos: List<ButterflyPhoto>) : Observable<Boolean>
    fun  getPhotosToPrint() : Observable<List<ButterflyPhoto>>
    fun  clear() : Observable<Boolean>
    fun delete(photo: ButterflyPhoto) : Observable<List<ButterflyPhoto>>
    fun saveContributionItem(item:ContributionItem) : Observable<Boolean>
    fun getContributionItems() : Observable<List<ContributionItem>>
    fun deleteContributionItems() : Observable<Boolean>
}

class CacheManager(override val prefs: SharedPreferences) : ICacheManager{

    override val photosToPrint: String = "photosToPrint"
    override val contributionItems: String = "contributionItems"

    override fun savePhotosToPrint(photos: List<ButterflyPhoto>): Observable<Boolean> {
        val gson = Gson()
        return Observable.just(gson.toJson(photos))
            .map{
                prefs.edit().putString(photosToPrint, it).apply()
            }
            .map{ true }
    }

    override fun getPhotosToPrint(): Observable<List<ButterflyPhoto>> {
        val gson = Gson()
        val sType = object : TypeToken<List<ButterflyPhoto>>() {}.type
        return Observable.just(prefs.getString(photosToPrint, "") ?: "")
            .map{
                return@map if(it!="")
                    gson.fromJson<List<ButterflyPhoto>>(it, sType)
                else
                    emptyList<ButterflyPhoto>()
            }
    }

    override fun clear(): Observable<Boolean> {
        return Observable.just(prefs.edit().remove(photosToPrint).apply())
            .map { true }
    }

    override fun delete(photo: ButterflyPhoto): Observable<List<ButterflyPhoto>> {
        return getPhotosToPrint()
            .map { photos ->
                photos.filter { ph ->
                    !(ph.familyId == photo.familyId && ph.specieId == photo.specieId && ph.id == photo.id)
                }
            }
            .flatMap { photos ->
                savePhotosToPrint(photos).map {
                    photos
                }
            }
    }

    override fun saveContributionItem(item: ContributionItem): Observable<Boolean> {
        val gson = Gson()
        return getContributionItems()
            .map{items ->
                items + item
            }
            .map{items ->
                gson.toJson(items)
            }
            .map{
                prefs.edit().putString(contributionItems, it).apply()
            }
            .map{ true }
    }

    override fun getContributionItems(): Observable<List<ContributionItem>> {
        val gson = Gson()
        val sType = object : TypeToken<List<ContributionItem>>() {}.type
        return Observable.just(prefs.getString(contributionItems, "") ?: "")
            .map{
                return@map if(it!="")
                    gson.fromJson(it, sType)
                else
                    emptyList()
            }
    }

    override fun deleteContributionItems(): Observable<Boolean> {
        return Observable.just(prefs.edit().remove(contributionItems).apply())
            .map { true }
    }
}