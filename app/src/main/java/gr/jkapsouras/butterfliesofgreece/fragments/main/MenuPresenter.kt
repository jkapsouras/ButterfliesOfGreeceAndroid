package gr.jkapsouras.butterfliesofgreece.fragments.main

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.GeneralViewState
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.fragments.main.ViewStates.MenuViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.main.events.MenuUiEvents

class MenuPresenter(
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    private val responseMessages:Map<MenuUiEvents, MenuViewStates> = mapOf(MenuUiEvents.FieldClicked to MenuViewStates.ToField,
        MenuUiEvents.IntroductionClicked to MenuViewStates.ToIntroduction,
        MenuUiEvents.AboutClicked to MenuViewStates.ToAbout,
        MenuUiEvents.ContributeClicked to MenuViewStates.ToContribute,
        MenuUiEvents.EndangeredSpeciesClicked to MenuViewStates.ToEndangered,
        MenuUiEvents.LegalClicked to MenuViewStates.ToLegal,
        MenuUiEvents.RecognitionClicked to MenuViewStates.ToRecognition)

    override fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is MenuUiEvents -> responseMessages[uiEvent]?.let{
                state.onNext(it)
            }
            else -> state.onNext(GeneralViewState.Idle)
        }
    }

    override fun setupEvents() {

    }

}