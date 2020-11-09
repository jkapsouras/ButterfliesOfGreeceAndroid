package gr.jkapsouras.butterfliesofgreece.fragments.contribute

import android.content.ContentValues.TAG
import android.util.Log
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.dto.ContributionItem
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.state.ContributeState
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.state.prepareHtmlForExport
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.uiEvents.ContributeEvents
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.viewStates.ContributeViewStates
import gr.jkapsouras.butterfliesofgreece.repositories.ContributionRepository
import gr.jkapsouras.butterfliesofgreece.repositories.FamiliesRepository
import gr.jkapsouras.butterfliesofgreece.repositories.NavigationRepository
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosToPrintRepository

class ContributePresenter(
//    val locationManager: LocationManager,
    val contributionRepository: ContributionRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    var contributeState: ContributeState = ContributeState(ContributionItem("", "", "", "", "", "", "", "", "", ""), "", "")


    override fun setupEvents() {

//        locationManager.getPermissionStatus().subscribe{state ->
//                when (state){
//            case .askLocation:
//            self.locationManager.askForLocation()
//            case .askPermission:
//            self.locationManager.askForPermissions()
//            case .showSettings:
//            self.state.onNext(ContributeViewStates.ShowSettingsDialog)
//            default:
//            break
//        }
//        }
//        .disposeBy(disposables)
//
//        locationManager.locationObs.observeOn(backgroundThreadScheduler.scheduler).subscribe{locationState ->
//                when (locationState){
//            case .showLocation(let location):
//            self.emitter.onNext(ContributeEvents.locationFetched(location: location))
//            case .locationErrored:
//            self.state.onNext(ContributeViewStates.showLocationError)
//            default:
//            break
//        }
//
//        }
//            .disposeBy(disposables)
    }

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is ContributeEvents -> {
                when (uiEvent) {
                    ContributeEvents.TextDateClicked ->
                        state.onNext(ContributeViewStates.ShowDatePicker)
                    is ContributeEvents.ButtonDoneClicked -> {
                        state.onNext(ContributeViewStates.HideDatePicker)
//                        let formatter : DateFormatter = DateFormatter ()
//                        formatter.dateFormat = "dd/MM/yyyy"
//                        let dateStr = formatter . string (from: date)
                        val dateStr = uiEvent.date.toString()
                        contributeState = contributeState.with(date = dateStr)
                        state.onNext(ContributeViewStates.SetDate(date = dateStr))
                    }
                    is ContributeEvents.LocationFetched -> {
                        contributeState = contributeState.with(
                            longitude =
                            (uiEvent.location.longitude).toString(),
                            latitude =
                            (uiEvent.location.latitude).toString()
                        )
                        state.onNext(
                            ContributeViewStates.ShowLocation(
                                latitude = contributeState.contributionItem.latitude ?: "",
                                longitude = contributeState.contributionItem.longitude ?: ""
                            )
                        )
                    }
                    is ContributeEvents.TextNameSet ->
                        contributeState = contributeState.with(name = uiEvent.name)
                    is ContributeEvents.TextAltitudeSet ->
                        contributeState = contributeState.with(altitude = uiEvent.altitude)
                    is ContributeEvents.TextPlaceSet ->
                        contributeState = contributeState.with(place = uiEvent.place)
                    is ContributeEvents.TextStateSet ->
                        contributeState = contributeState.with(stage = uiEvent.stage)
                    is ContributeEvents.TextGenusSpeciesSet ->
                        contributeState = contributeState.with(genusSpecies = uiEvent.genusSpecies)
                    is ContributeEvents.TextNameSpeciesSet ->
                        contributeState = contributeState.with(nameSpecies = uiEvent.nameSpecies)
                    is ContributeEvents.TextCommentsSet ->
                        contributeState = contributeState.with(comments = uiEvent.comments)
                    is ContributeEvents.TextLatitudeSet ->
                        contributeState = contributeState.with(latitude = uiEvent.latitude)
                    is ContributeEvents.TextLongitudeSet ->
                        contributeState = contributeState.with(longitude = uiEvent.longitude)
                    ContributeEvents.AddClicked ->
                        contributionRepository.saveContributionItem(item = contributeState.contributionItem)
                            .subscribeOn(backgroundThreadScheduler.scheduler)
                            .subscribe { done ->
                                if (done)
                                    state.onNext(ContributeViewStates.ShowItemAdded)
                                else
                                    state.onNext(ContributeViewStates.ShowItemNotAdded)
                            }
                            .disposeWith(disposables)
                    ContributeEvents.ExportClicked -> {
                        contributionRepository.getContributionItems()
                            .doOnNext { items ->
                                contributeState =
                                    contributeState.prepareHtmlForExport(items = items)
//                            let pdfManager = PdfManager ()
//                            self.contributeState =
//                                self.contributeState.with(pdfData:pdfManager. createRecordsTable (html: self.contributeState.exportedHtml, printRenderer: UIPrintPageRenderer()))
//                            self.state.onNext(ContributeViewStates.showExtractedPdf(pdfData: self. contributeState . pdfData ?? Data ()))
                            }
                            .flatMap {
                                contributionRepository.delete()
                            }
                            .subscribe { Log.d(TAG, "all good") }
                            .disposeWith(disposables)
                    }
                    ContributeEvents.SharePdf ->
                        state.onNext(
                            ContributeViewStates.ShowShareDialog(
                                pdfData = contributeState.pdfData ?: ""
                            )
                        )
                    ContributeEvents.InstructionsClicked ->
                        state.onNext(ContributeViewStates.ShowInstructions)
                    ContributeEvents.ClosePdf ->
                        state.onNext(ContributeViewStates.ClosePdf)
                }
            }
        }
    }
}