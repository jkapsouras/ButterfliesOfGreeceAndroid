package gr.jkapsouras.butterfliesofgreece.base.di

import gr.jkapsouras.butterfliesofgreece.base.BasePresenter
import gr.jkapsouras.butterfliesofgreece.base.schedulers.BackgroundThreadScheduler
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.MainThreadScheduler
import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.families.FamiliesPresenter
import gr.jkapsouras.butterfliesofgreece.main.MenuPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val butterfliesModule = module {
    registerSchedulers(this)
    registerDataSources(this)
    registerPresenters(this)
}

fun registerSchedulers(module: Module){
    module.single<IBackgroundThread> { BackgroundThreadScheduler(Schedulers.newThread()) }
    module.single<IMainThread> { MainThreadScheduler(AndroidSchedulers.mainThread())}
}

fun registerDataSources(module: Module){
    module.single { Storage(androidContext()) }
}

fun registerPresenters(module: Module){
    module.factory{ MenuPresenter(
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get(),
        presenter = Presenters.Menu
    ) }
    module.factory { FamiliesPresenter(
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get(),
        presenter = Presenters.Families) }
}

enum class Presenters{
    Menu,
    Families
}