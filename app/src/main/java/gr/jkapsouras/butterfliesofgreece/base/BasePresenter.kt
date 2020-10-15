package gr.jkapsouras.butterfliesofgreece.base

import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BasePresenter(private val backgroundThreadScheduler : IBackgroundThread,
                             private val mainThreadScheduler:IMainThread) {
    lateinit var disposables: DisposablesWrapper
    private val state: PublishSubject<ViewState> = PublishSubject.create<ViewState>()
    private val emitter: PublishSubject<ViewState> = PublishSubject.create<ViewState>()

    fun subscribe(events:Observable<UiEvent>):Observable<ViewState>{
        disposables = DisposablesWrapper()


        Observable.merge(events, emitter)
            .subscribe()
            .disposeWith(disposables)
        return state.observeOn(mainThreadScheduler.scheduler)
    }

    abstract fun handleEvent(uiEvents:UiEvent)

    abstract fun setupEvents()

    fun unSubscribe(){
        disposables.dispose()
    }
}