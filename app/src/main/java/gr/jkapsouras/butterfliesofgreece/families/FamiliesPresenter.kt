package gr.jkapsouras.butterfliesofgreece.families

import android.content.ContentValues.TAG
import android.util.Log
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.repositories.FamiliesRepository
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.families.state.FamiliesState
import gr.jkapsouras.butterfliesofgreece.families.state.with
import gr.jkapsouras.butterfliesofgreece.families.uiEvents.FamilyEvents
import gr.jkapsouras.butterfliesofgreece.families.viewStates.FamiliesViewViewStates
import gr.jkapsouras.butterfliesofgreece.views.header.HeaderState
import gr.jkapsouras.butterfliesofgreece.views.header.uiEvents.HeaderViewEvents
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.HeaderViewViewStates

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

    private var familiesState: FamiliesState = FamiliesState(emptyList())
    private val headerState: HeaderState = HeaderState(null, ViewArrange.Grid, "Families")

    override fun handleEvent(uiEvent: UiEvent) {
        when(uiEvent){
          is  FamilyEvents -> {
              handleFamiliesEvents(uiEvent)
          }
            is HeaderViewEvents -> {
                handleHeaderViewEvents(uiEvent)
            }
        }
    }

    override fun setupEvents() {
        emitter.onNext(FamilyEvents.LoadFamilies)
        emitter.onNext(HeaderViewEvents.InitState(ViewArrange.List))
    }

    private fun handleFamiliesEvents(familyEvent: FamilyEvents)
    {
        when(familyEvent){
            is FamilyEvents.FamilyClicked -> {
            Log.d(TAG, "navigation repository not yet implemented")
        }
            is FamilyEvents.LoadFamilies -> {
                familiesRepository
                    .getAllFamilies()
                    .map { families ->
                        familiesState.with(families)
                    }
                    .subscribe { familiesState ->
                        state.onNext(FamiliesViewViewStates.ShowFamilies(familiesState.families))
                    }
                    .disposeWith(disposables)
            }
            else -> {
                Log.d(TAG, "handleFamiliesEvents: something else")
            }
        }
    }

    private fun handleHeaderViewEvents(headerEvent: HeaderViewEvents){
        when(headerEvent){
            is HeaderViewEvents.SearchBarClicked -> {
                state.onNext(HeaderViewViewStates.ToSearch)
            }
            is HeaderViewEvents.PrintPhotosClicked -> {
                state.onNext(HeaderViewViewStates.ToPrintPhotos)
            }
}
    }
}