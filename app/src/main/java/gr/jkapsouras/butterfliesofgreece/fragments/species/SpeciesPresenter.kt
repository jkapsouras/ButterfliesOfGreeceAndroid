package gr.jkapsouras.butterfliesofgreece.fragments.species

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.repositories.NavigationRepository
import gr.jkapsouras.butterfliesofgreece.repositories.SpeciesRepository
import gr.jkapsouras.butterfliesofgreece.fragments.species.state.SpeciesState
import gr.jkapsouras.butterfliesofgreece.fragments.species.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.species.uiEvents.SpeciesEvents
import gr.jkapsouras.butterfliesofgreece.fragments.species.viewStates.SpeciesViewStates
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosToPrintRepository
import gr.jkapsouras.butterfliesofgreece.views.header.HeaderState
import gr.jkapsouras.butterfliesofgreece.views.header.uiEvents.HeaderViewEvents
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.FromFragment
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.HeaderViewViewStates
import gr.jkapsouras.butterfliesofgreece.views.header.with
import io.reactivex.rxjava3.core.Observable

class SpeciesPresenter(
    private val speciesRepository: SpeciesRepository,
    private val navigationRepository: NavigationRepository,
    private  val photosToPrintRepository: PhotosToPrintRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    private var speciesState: SpeciesState = SpeciesState(emptyList())
    private var headerState: HeaderState = HeaderState(null, ViewArrange.Grid, "Species")

    override fun setupEvents() {
        speciesState = SpeciesState(emptyList())
        headerState = HeaderState(null,  ViewArrange.Grid, "Species")
        Observable.zip(navigationRepository.getFamilyId(),navigationRepository.getViewArrange(),
            { familyId, currentArrange ->
                Pair(familyId, currentArrange)
            })
            .subscribe {data ->
                emitter.onNext(SpeciesEvents.LoadSpecies(data.first))
                emitter.onNext(HeaderViewEvents.InitState( data.second))}
    }

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is SpeciesEvents ->
                handleSpeciesEvents(uiEvent)
            is HeaderViewEvents ->
                handleHeaderViewEvents(uiEvent)
            else ->
                state.onNext(GeneralViewState.Idle)
        }
    }

    private fun handleSpeciesEvents(specieEvent: SpeciesEvents){
        when (specieEvent) {
            is SpeciesEvents.SpecieClicked ->
                navigationRepository
                    .selectSpecieId(specieEvent.id)
                    .subscribe {
                        state.onNext(SpeciesViewStates.ToPhotos)
                    }
                    .disposeWith(disposables)
            is SpeciesEvents.LoadSpecies ->
                Observable.zip(speciesRepository.getSelectedFamilyName(specieEvent.familyId)
                    .map { familyName ->
                        headerState = headerState.with(null, null, familyName)
                        headerState
                    },
                    speciesRepository.getSpeciesOfFamily(specieEvent.familyId).map { species ->
                        speciesState = speciesState.with(species)
                        speciesState
                    }, { headerState, speciesState ->
                        Pair(headerState, speciesState)
                    })
                    .subscribe { data ->
                        state.onNext(HeaderViewViewStates.SetHeaderTitle(data.first.headerName))
                        state.onNext(SpeciesViewStates.ShowSpecies(data.second.species, false))
                    }
                    .disposeWith(disposables)
            is SpeciesEvents.AddPhotosForPrintClicked -> {
                photosToPrintRepository
                    .getPhotosToPrint()
                    .map{photos ->
                        updateHeaderState(photos, specieEvent.specieId)}
                    .flatMap{photoState ->
                        photosToPrintRepository.savePhotosToPrint(photoState.photosToPrint ?: emptyList())}
                    .subscribe{headerState ->
                        state.onNext(HeaderViewViewStates.UpdateFolderIcon(headerState.count()))}
                    .disposeWith(disposables)
            }
        }
    }

    private fun updateHeaderState(photos:List<ButterflyPhoto>, specieId:Int) : HeaderState{
        headerState = headerState.with(null, photos)
        val specie = speciesState.species.first{it.id == specieId}
        val newPhotos = specie.photos
                headerState = headerState.with(null, newPhotos)
        return headerState
    }

    private fun handleHeaderViewEvents(headerEvent: HeaderViewEvents){
        when (headerEvent) {
            is HeaderViewEvents.InitState ->{
                photosToPrintRepository.getPhotosToPrint().map{photos ->
                        headerState = headerState.with(currentArrange = headerEvent.currentArrange, photosToPrint = photos)
                    headerState
                }.subscribe{
                    state.onNext(HeaderViewViewStates.UpdateFolderIcon(numberOfPhotos = it.photosToPrint?.count() ?: 0))
                    state.onNext(SpeciesViewStates.SwitchViewStyle(currentArrange = it.currentArrange))
                }
            }
            is HeaderViewEvents.SwitchViewStyleClicked -> {
                navigationRepository.changeViewArrange()
                    .map{arrange ->
                            headerState = headerState.with(arrange)
                        headerState
                    }
                    .subscribe{headerState ->
                        state.onNext(SpeciesViewStates.SwitchViewStyle(headerState.currentArrange))}
                    .disposeWith(disposables)
            }
            is HeaderViewEvents.SearchBarClicked ->
                state.onNext(HeaderViewViewStates.ToSearch(FromFragment.Species))
            is  HeaderViewEvents.PrintPhotosClicked ->
                state.onNext(HeaderViewViewStates.ToPrintPhotos(FromFragment.Species))
        }
    }
}