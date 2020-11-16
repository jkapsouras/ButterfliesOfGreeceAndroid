package gr.jkapsouras.butterfliesofgreece.fragments.legal

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.legal.uiEvents.LegalEvents
import gr.jkapsouras.butterfliesofgreece.fragments.legal.viewStates.LegalViewStates

class LegalPresenter(backgroundThreadScheduler: IBackgroundThread,
                     mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    private val termsPdf = "127557_2178_2015.pdf"
    private val formsPdf = "135366-16.pdf"

    override fun setupEvents() {
        state.onNext(LegalViewStates.ShowTermsPdf(document = termsPdf))
        state.onNext(LegalViewStates.ShowPopup)
    }

    override fun handleEvent(uiEvents: UiEvent) {
        when (uiEvents) {
            is LegalEvents -> {
                when (uiEvents) {
                    LegalEvents.TermsClicked -> {
                        state.onNext(LegalViewStates.ShowTermsPdf(document = termsPdf))
                        state.onNext(LegalViewStates.ShowPopup)
                    }
                    LegalEvents.FormsClicked -> {
                        state.onNext(LegalViewStates.ShowFormsPdf(document = formsPdf))
                        state.onNext(LegalViewStates.ShowPopup)
                    }
                    LegalEvents.OkClicked ->
                    state.onNext(LegalViewStates.HidePopup)
                }
            }
        }
    }

}