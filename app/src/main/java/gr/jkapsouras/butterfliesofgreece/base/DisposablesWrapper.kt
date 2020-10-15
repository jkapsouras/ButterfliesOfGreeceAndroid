package gr.jkapsouras.butterfliesofgreece.base

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

class DisposablesWrapper {
    private var compositeDisposable: CompositeDisposable? = null

    public fun add(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    fun dispose() {
        compositeDisposable?.dispose()
        compositeDisposable = null
    }
}

fun Disposable.disposeWith(androidDisposable: DisposablesWrapper): Disposable
        = apply { androidDisposable.add(this) }