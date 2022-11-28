package gr.jkapsouras.butterfliesofgreece.fragments.modal.components

import android.content.ContentValues
import android.util.Log
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.modal.IModalPhotos
import gr.jkapsouras.butterfliesofgreece.fragments.modal.viewStates.ModalViewStates
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class ModalPhotoComponent(private val owner:IModalPhotos) : UiComponent {
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent> = Observable.never()

    override fun renderViewState(viewState: ViewState) {
        if (viewState is ModalViewStates) {
            when (viewState) {
                is ModalViewStates.ShowPhotosStartingWith ->
                    owner.setUpPagesStartingWith(index = viewState.index, photos = viewState.photos)
                else ->
                    Log.d(ContentValues.TAG, "nothing")
            }
        }
    }
}