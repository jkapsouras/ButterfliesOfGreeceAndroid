package gr.jkapsouras.butterfliesofgreece.base

import gr.jkapsouras.butterfliesofgreece.base.schedulers.IBackgroundThread
import gr.jkapsouras.butterfliesofgreece.base.schedulers.IMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BasePresenter(
    internal val backgroundThreadScheduler : IBackgroundThread,
    private val mainThreadScheduler:IMainThread) {
    lateinit var disposables: DisposablesWrapper
    protected val state: PublishSubject<ViewState> = PublishSubject.create<ViewState>()
    internal val emitter: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()

    fun subscribe(events:Observable<UiEvent>):Observable<ViewState>{
        disposables = DisposablesWrapper()


        Observable.merge(events, emitter)
            .subscribe{event -> handleEvent(event)}
            .disposeWith(disposables)
        return state.observeOn(mainThreadScheduler.scheduler)
    }

    abstract fun handleEvent(uiEvent:UiEvent)

    abstract fun setupEvents()

    fun unSubscribe(){
        disposables.dispose()
    }
}