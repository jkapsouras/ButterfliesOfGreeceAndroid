package gr.jkapsouras.butterfliesofgreece.base.di

import android.content.Context
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.schedulers.BackgroundThreadScheduler
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.MainThreadScheduler
import gr.jkapsouras.butterfliesofgreece.data.Storage
import gr.jkapsouras.butterfliesofgreece.fragments.families.FamiliesPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.main.MenuPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.modal.ModalPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.photos.PhotosPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.previewer.PdfPreviewPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.PrintToPdfPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.search.SearchPresenter
import gr.jkapsouras.butterfliesofgreece.fragments.species.SpeciesPresenter
import gr.jkapsouras.butterfliesofgreece.managers.CacheManager
import gr.jkapsouras.butterfliesofgreece.managers.ICacheManager
import gr.jkapsouras.butterfliesofgreece.repositories.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val butterfliesModule = module {
    registerSchedulers(this)
    registerDataSources(this)
    registerRepositories(this)
    registerPresenters(this)
}

fun registerSchedulers(module: Module){
    module.single<IBackgroundThread> { BackgroundThreadScheduler(Schedulers.newThread()) }
    module.single<IMainThread> { MainThreadScheduler(AndroidSchedulers.mainThread())}
}

fun registerDataSources(module: Module){
    module.single { Storage(androidContext()) }
    module.single<ICacheManager>{CacheManager(androidContext().getSharedPreferences(androidContext().resources.getString(
        R.string.app_name), Context.MODE_PRIVATE))}
}

fun registerRepositories(module: Module){
    module.factory { FamiliesRepository(
        storage = get()
    ) }
    module.factory { NavigationRepository(
        storage = get()
    ) }
    module.factory { SpeciesRepository(
        storage = get()
    ) }
    module.factory { PhotosRepository(
        storage = get()
    ) }
    module.factory { PhotosToPrintRepository(
        storage = get(),
        cacheManager = get()
    ) }
}

fun registerPresenters(module: Module){
    module.factory{ MenuPresenter(
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
    module.factory { FamiliesPresenter(
        familiesRepository = get(),
        navigationRepository = get(),
        photosToPrintRepository = get(),
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
    module.factory{ SpeciesPresenter(
        speciesRepository = get(),
        navigationRepository = get(),
        photosToPrintRepository = get(),
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
    module.factory{ PhotosPresenter(
        photosRepository = get(),
        navigationRepository = get(),
        photosToPrintRepository = get(),
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
    module.factory{ SearchPresenter(
        speciesRepository = get(),
        navigationRepository = get(),
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
    module.factory{ ModalPresenter(
        photosRepository = get(),
        navigationRepository = get(),
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
    module.factory{ PrintToPdfPresenter(
        photosToPrintRepository = get(),
        navigationRepository = get(),
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
    module.factory{ PdfPreviewPresenter(
        photosToPrintRepository = get(),
        backgroundThreadScheduler = get(),
        mainThreadScheduler =  get()
    ) }
}