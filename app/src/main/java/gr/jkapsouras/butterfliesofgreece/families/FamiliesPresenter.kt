package gr.jkapsouras.butterfliesofgreece.families

import android.content.ContentValues.TAG
import android.util.Log
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.repositories.FamiliesRepository
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.families.uiEvents.FamilyEvents

enum class ViewArrange{
    List,
    Grid;

    companion object{
        fun changeArrange(viewArrange: ViewArrange):ViewArrange{
            return when (viewArrange) {
                List ->
                    Grid
                Grid ->
                    List
            }
        }
    }
}

class FamiliesPresenter(
    val familiesRepository: FamiliesRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    override fun handleEvent(uiEvent: UiEvent) {
        when(uiEvent){
          is  FamilyEvents -> {
              handleFamiliesEvents(uiEvent)
          }
        }
    }

    override fun setupEvents() {
        emitter.onNext(FamilyEvents.LoadFamilies)
    }

    private fun handleFamiliesEvents(familyEvent: FamilyEvents)
    {
        when(familyEvent){
            is FamilyEvents.LoadFamilies -> {
                Log.d(TAG, "handleFamiliesEvents: is families")
            }
            else -> {
                Log.d(TAG, "handleFamiliesEvents: something else")
            }
        }
    }
}