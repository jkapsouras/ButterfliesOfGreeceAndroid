package gr.jkapsouras.butterfliesofgreece.fragments.recognition.component

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.viewStates.RecognitionViewStates
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RecognitionComponent : UiComponent {

    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = event
    }

    override fun renderViewState(viewState: ViewState) {
        if (viewState is RecognitionViewStates) {
            when (viewState) {
                is RecognitionViewStates.ShowGallery -> {
                }
                else ->
                    print("default state")
            }
        }
    }
}