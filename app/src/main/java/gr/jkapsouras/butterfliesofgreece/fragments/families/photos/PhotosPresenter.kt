package gr.jkapsouras.butterfliesofgreece.fragments.families.photos

import android.util.Log
import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.state.PhotosState
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.state.with
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.uiEvents.PhotosEvents
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.viewStates.PhotosViewStates
import gr.jkapsouras.butterfliesofgreece.repositories.NavigationRepository
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosRepository
import gr.jkapsouras.butterfliesofgreece.views.header.HeaderState
import gr.jkapsouras.butterfliesofgreece.views.header.uiEvents.HeaderViewEvents
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.HeaderViewViewStates
import gr.jkapsouras.butterfliesofgreece.views.header.with
import io.reactivex.rxjava3.core.Observable

class PhotosPresenter(
    private val photosRepository: PhotosRepository,
    private val navigationRepository: NavigationRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler) {

    private var photosState: PhotosState = PhotosState(emptyList(), -1)
    private var headerState: HeaderState = HeaderState(null, ViewArrange.Grid, "Photos")

    override fun setupEvents() {
        Observable.zip(navigationRepository.getSpecieId(), navigationRepository.getViewArrange(),
            { specieId, currentArrange ->
                Pair(specieId, currentArrange)
            })
            .subscribe { data ->
                emitter.onNext(PhotosEvents.LoadPhotos(data.first))
                emitter.onNext(HeaderViewEvents.InitState(data.second))
            }
            .disposeWith(disposables)
    }

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is PhotosEvents ->
                handlePhotosEvents(uiEvent)
            is HeaderViewEvents ->
                handleHeaderViewEvents(uiEvent)
            else ->
                state.onNext(GeneralViewState.Idle)
        }
    }

    private fun handlePhotosEvents(photosEvent: PhotosEvents) {
        when (photosEvent) {
            is PhotosEvents.PhotoClicked ->
                navigationRepository
                    .selectPhotoId(photosEvent.id)
                    .subscribe {
                        state.onNext(PhotosViewStates.ToPhoto(photosEvent.id))
                    }
                    .disposeWith(disposables)
            is PhotosEvents.LoadPhotos ->
                Observable.zip(photosRepository.getSelectedSpecieName(photosEvent.specieId)
                    .map { specieName ->
                        headerState = headerState.with(headerName = specieName)
                        headerState
                    },
                    photosRepository.getPhotosOfSpecie(photosEvent.specieId).map { photos ->
                        photosState = photosState.with(photos = photos)
                        photosState
                    },
                    { headerState, photosState ->
                        Pair(headerState, photosState)
                    }
                )
                    .subscribe { data ->
                        state.onNext(HeaderViewViewStates.SetHeaderTitle(data.first.headerName))
                        state.onNext(PhotosViewStates.ShowPhotos(data.second.photos))
                    }
                    .disposeWith(disposables)
            is PhotosEvents.AddPhotoForPrintClicked ->
                Log.println(Log.INFO, "", "clicked")
//                _ = photosToPrintRepository
//                    .getPhotosToPrint()
//                    .map {
//                        photos in return self.updateHeaderState(
//                            photos: photos,
//                            photoId: photoId
//                        )
//                    }
//                    .do (onNext
//                : {
//                photoState in self.photosToPrintRepository.savePhotosToPrint(photos: photoState. photosToPrint ?? [ButterflyPhoto]())
//            }
//                )
//                .subscribe(onNext: {
//                headerState in self.state.onNext(
//                    HeaderViewViewStates.updateFolderIcon(
//                        numberOfPhotos: headerState. photosToPrint !. count
//                ))
//            }
//                )
        }
    }

    private fun handleHeaderViewEvents(headerEvent: HeaderViewEvents){
        when (headerEvent) {
            is HeaderViewEvents.InitState -> {
                headerState = headerState.with(headerEvent.currentArrange, emptyList())
                state.onNext(PhotosViewStates.SwitchViewStyle(headerState.currentArrange))
            }
//            _ = photosToPrintRepository.getPhotosToPrint().map{photos -> HeaderState in
//                    self.headerState = self.headerState.with(arrange: currentArrange, photos: photos)
//                return self.headerState
//            }.subscribe(onNext: {headerState in self.state.onNext(HeaderViewViewStates.updateFolderIcon(numberOfPhotos: headerState.photosToPrint?.count ?? 0))
//                self.state.onNext(PhotosViewStates.SwitchViewStyle(currentArrange: headerState.currentArrange))
//
//            })
            is HeaderViewEvents.SwitchViewStyleClicked -> {
                navigationRepository.changeViewArrange()
                    .map{arrange ->
                        headerState = headerState.with(arrange)
                        headerState
                    }
                    .subscribe{headerState ->
                        state.onNext(PhotosViewStates.SwitchViewStyle(headerState.currentArrange))}
                    .disposeWith(disposables)
            }
            is HeaderViewEvents.SearchBarClicked ->
            state.onNext(HeaderViewViewStates.ToSearch)
            is HeaderViewEvents.PrintPhotosClicked ->
            state.onNext(HeaderViewViewStates.ToPrintPhotos)
        }
    }

    private fun updateHeaderState(photos:List<ButterflyPhoto>, photoId:Int) : HeaderState{
        headerState = headerState.with(photosToPrint = photos)
        val photo = photosState.photos.first{it.id == photoId}
        val newPhotos = mutableListOf<ButterflyPhoto>()
        newPhotos.add(photo)
        headerState = headerState.with(photosToPrint = newPhotos)
        return headerState
    }
}