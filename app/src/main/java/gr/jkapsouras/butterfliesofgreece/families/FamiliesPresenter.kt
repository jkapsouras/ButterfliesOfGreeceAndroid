package gr.jkapsouras.butterfliesofgreece.families

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.di.Presenters
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread

class FamiliesPresenter(
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread, presenter: Presenters
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    override fun handleEvent(uiEvent: UiEvent) {

    }

    override fun setupEvents() {

    }

}