package gr.jkapsouras.butterfliesofgreece.fragments.families.main.ViewStates

import gr.jkapsouras.butterfliesofgreece.base.ViewState

sealed class MenuViewStates(isTransition:Boolean) : ViewState(isTransition) {
    object ToField : MenuViewStates(true)

    object ToIntroduction : MenuViewStates(true)

    object ToEndangered : MenuViewStates(true)

    object ToLegal : MenuViewStates(true)

    object ToAbout : MenuViewStates(true)

    object ToContribute : MenuViewStates(true)

    object ToOnlineRecognition : MenuViewStates(true)

    object ToOfflineRecognition : MenuViewStates(true)

    object ToRecognition : MenuViewStates(true)
}