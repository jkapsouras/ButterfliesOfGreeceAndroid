package gr.jkapsouras.butterfliesofgreece.base

import io.reactivex.rxjava3.core.Observable

interface UiComponent{
    val  uiEvents: Observable<UiEvent>
    fun renderViewState(viewState:ViewState)
}