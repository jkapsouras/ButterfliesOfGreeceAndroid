package gr.jkapsouras.butterfliesofgreece.repositories

import gr.jkapsouras.butterfliesofgreece.dto.ContributionItem
import gr.jkapsouras.butterfliesofgreece.managers.ICacheManager
import io.reactivex.rxjava3.core.Observable

class ContributionRepository(private val cacheManager: ICacheManager)  {

    fun getContributionItems() : Observable<List<ContributionItem>>{
        return cacheManager.getContributionItems()
    }

    fun saveContributionItem(item:ContributionItem) : Observable<Boolean>{
        return cacheManager.saveContributionItem(item = item)
    }

    fun delete() : Observable<Boolean>{
        return cacheManager.deleteContributionItems()
    }
}
