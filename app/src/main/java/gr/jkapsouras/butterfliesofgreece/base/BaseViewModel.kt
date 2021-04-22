package gr.jkapsouras.butterfliesofgreece.base

abstract class UiEvent

abstract class ViewState(val isTransition: Boolean)

sealed class GeneralViewState(isTransition:Boolean) : ViewState(isTransition) {
    object Idle : GeneralViewState(true)
}