package gr.jkapsouras.butterfliesofgreece.fragments.families

import android.location.LocationManager
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.repositories.FamiliesRepository
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.families.state.FamiliesState
import gr.jkapsouras.butterfliesofgreece.fragments.families.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.families.uiEvents.FamilyEvents
import gr.jkapsouras.butterfliesofgreece.fragments.families.viewStates.FamiliesViewViewStates
import gr.jkapsouras.butterfliesofgreece.managers.ILocationManager
import gr.jkapsouras.butterfliesofgreece.repositories.NavigationRepository
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosToPrintRepository
import gr.jkapsouras.butterfliesofgreece.views.header.HeaderState
import gr.jkapsouras.butterfliesofgreece.views.header.uiEvents.HeaderViewEvents
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.FromFragment
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.HeaderViewViewStates
import gr.jkapsouras.butterfliesofgreece.views.header.with
import io.reactivex.rxjava3.core.Observable

enum class ViewArrange{
    List,
    Grid;

    companion object{
        fun changeArrange(viewArrange: ViewArrange): ViewArrange {
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
    val navigationRepository: NavigationRepository,
    val photosToPrintRepository: PhotosToPrintRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    private var familiesState: FamiliesState = FamiliesState(emptyList())
    private var headerState: HeaderState = HeaderState(null, ViewArrange.Grid, "Families")

    override fun handleEvent(uiEvent: UiEvent) {
        when(uiEvent){
          is FamilyEvents -> {
              handleFamiliesEvents(uiEvent)
          }
            is HeaderViewEvents -> {
                handleHeaderViewEvents(uiEvent)
            }
        }
    }

    private fun updateHeaderState(photos:List<ButterflyPhoto>, familyId:Int) : HeaderState{
        headerState = headerState.with(photosToPrint = photos)
        val family = familiesState.families.firstOrNull{it.id==familyId}
        val newPhotos = family?.species?.flatMap{it.photos}
        headerState = headerState.with(photosToPrint = newPhotos)
        return headerState
    }

    override fun setupEvents() {
        familiesState = FamiliesState(emptyList())
        headerState = HeaderState(null, headerState.currentArrange, "Families")
        emitter.onNext(FamilyEvents.LoadFamilies)
        emitter.onNext(HeaderViewEvents.InitState(ViewArrange.List))
    }

    private fun handleFamiliesEvents(familyEvent: FamilyEvents)
    {
        when(familyEvent){
            is FamilyEvents.FamilyClicked -> {
            navigationRepository
                .selectFamilyId(familyEvent.id)
                .subscribe{
                    state.onNext(FamiliesViewViewStates.ToSpecies)}
                .disposeWith(disposables)
        }
            is FamilyEvents.LoadFamilies -> {
                familiesRepository
                    .getAllFamilies()
                    .map { families ->
                       familiesState = familiesState.with(families)
                        familiesState
                    }
                    .subscribe { familiesState ->
                        state.onNext(FamiliesViewViewStates.ShowFamilies(familiesState.families))
                    }
                    .disposeWith(disposables)
            }
            is FamilyEvents.AddPhotosForPrintClicked -> {
                photosToPrintRepository
                    .getPhotosToPrint()
                    .map{photos ->
                        updateHeaderState(photos, familyEvent.familyId)}
                    .flatMap{photoState ->
                        photosToPrintRepository.savePhotosToPrint(photoState.photosToPrint ?: emptyList())}
                .subscribe{headerState ->
                    state.onNext(HeaderViewViewStates.UpdateFolderIcon(headerState.count()))}
                    .disposeWith(disposables)
            }
        }
    }

    private fun handleHeaderViewEvents(headerEvent: HeaderViewEvents){
        when(headerEvent){
            is HeaderViewEvents.InitState -> {
                Observable.zip(navigationRepository.setViewArrange(headerEvent.currentArrange)
                    .flatMap { navigationRepository.getViewArrange() }
                    .map{
                        headerState = headerState.with(currentArrange =  it)
                    headerState},
                photosToPrintRepository.getPhotosToPrint()
                    .map{photos ->
                        headerState = headerState.with(photosToPrint = photos)
                    headerState
                },{_, header ->
                        header
                    })
                .subscribe{headerState ->
                    state.onNext(HeaderViewViewStates.UpdateFolderIcon(headerState.photosToPrint!!.count()))
                    state.onNext(FamiliesViewViewStates.SwitchViewStyle(headerState.currentArrange))
                }
                    .disposeWith(disposables)
            }
            is HeaderViewEvents.SwitchViewStyleClicked -> {
                navigationRepository.changeViewArrange()
                    .map{arrange ->
                        headerState = headerState.with((arrange))
                        headerState
                }
                    .subscribe { headerState ->
                        state.onNext(FamiliesViewViewStates.SwitchViewStyle(headerState.currentArrange))
                    }
                    .disposeWith(disposables)
            }
            is HeaderViewEvents.SearchBarClicked -> {
                state.onNext(HeaderViewViewStates.ToSearch(FromFragment.Families))
            }
            is HeaderViewEvents.PrintPhotosClicked -> {
                state.onNext(HeaderViewViewStates.ToPrintPhotos(FromFragment.Families))
            }
        }
    }
}