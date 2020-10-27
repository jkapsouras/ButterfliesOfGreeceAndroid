package gr.jkapsouras.butterfliesofgreece.species

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread

class SpeciesPresenter(
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler){

    override fun handleEvent(uiEvent: UiEvent) {
//        TODO("Not yet implemented")
    }

    override fun setupEvents() {
//        TODO("Not yet implemented")
    }

}