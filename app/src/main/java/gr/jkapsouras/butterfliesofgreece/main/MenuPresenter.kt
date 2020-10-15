package gr.jkapsouras.butterfliesofgreece.main

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import org.koin.core.KoinComponent
import org.koin.core.inject

class MenuPresenter(
    backgroundThreadScheduler: IBackgroundThread,
    mainThreadScheduler: IMainThread
) : BasePresenter(backgroundThreadScheduler, mainThreadScheduler), KoinComponent{

    override fun handleEvent(uiEvents: UiEvent) {
        TODO("Not yet implemented")
    }

    override fun setupEvents() {
        TODO("Not yet implemented")
    }

}