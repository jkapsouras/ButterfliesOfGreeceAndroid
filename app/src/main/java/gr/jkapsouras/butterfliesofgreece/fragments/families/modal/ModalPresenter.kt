package gr.jkapsouras.butterfliesofgreece.fragments.families.modal

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.disposeWith
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.families.modal.uiEvents.ModalEvents
import gr.jkapsouras.butterfliesofgreece.fragments.families.modal.viewStates.ModalViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.state.PhotosState
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.state.with
import gr.jkapsouras.butterfliesofgreece.repositories.NavigationRepository
import gr.jkapsouras.butterfliesofgreece.repositories.PhotosRepository
import io.reactivex.rxjava3.core.Observable

class ModalPresenter(
    private val photosRepository: PhotosRepository,
    private val navigationRepository: NavigationRepository,
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler) {

    private var photosState = PhotosState(emptyList(), -1)

    override fun setupEvents() {
        Observable.zip(navigationRepository.getSpecieId(),navigationRepository.getPhotoId(),
            { specieId, photoId ->
                Pair(specieId, photoId)})
        .subscribe{data ->
                emitter.onNext(ModalEvents.LoadPhotos(specieId = data.first, photoId = data.second))
        }
            .disposeWith(disposables)
    }

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is ModalEvents ->
            handleModalEvents(modalEvent = uiEvent)
            else ->
            state.onNext(GeneralViewState.Idle)
        }
    }

    private fun handleModalEvents(modalEvent: ModalEvents){
        when (modalEvent) {
            is ModalEvents.LoadPhotos ->
            photosRepository.getPhotosOfSpecie(specieId = modalEvent.specieId).map{photos ->
                    photosState = photosState.with(photos = photos, photoId = modalEvent.photoId)
                photosState}
                .subscribe{data ->
                        state.onNext(ModalViewStates.ShowPhotosStartingWith(index = data.indexOfSelectedPhoto, photos = data.photos.map{it.source}))}
                .disposeWith(disposables)
            is ModalEvents.CloseModalClicked ->
                state.onNext(ModalViewStates.CloseModal)
        }
    }
}
