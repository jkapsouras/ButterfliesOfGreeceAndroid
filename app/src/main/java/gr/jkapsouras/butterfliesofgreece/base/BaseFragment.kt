package gr.jkapsouras.butterfliesofgreece.base

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Observable
import org.koin.android.ext.android.inject


abstract  class BaseFragment<P> : Fragment() where P : BasePresenter{

    private val _presenter: BasePresenter by inject()
    private var presenter: P? = null
        get() {
            return _presenter as P?
        }
    lateinit var events:Observable<UiEvent>
    lateinit var components:List<UiComponent>

    abstract fun  initializeComponents(constraintLayout: ConstraintLayout):List<UiComponent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        components = initializeComponents(view as ConstraintLayout)

    }

    override fun onStart() {
        super.onStart()
        events = Observable.empty()
        components.forEach{
            events = Observable.merge(events,it.uiEvents)
        }

        val presenter = presenter ?: return

        val state = presenter?.subscribe(events)?.publish()

        components?.forEach{component->
                    state?.subscribe { viewstate->
                        component.renderViewState(viewstate)
                    }
                        ?.disposeWith(presenter.disposables)
        }

         state?.filter { it.isTransition }?.subscribe{ viewState -> transitionStateReceived(viewState)}?.disposeWith(presenter.disposables)
        state?.connect()?.disposeWith(presenter.disposables)
        presenter.setupEvents()
    }

    private fun transitionStateReceived(viewState:ViewState){
        Log.d(TAG, "transitionStateReceived: ${viewState.toString()}")
    }

    fun unSubscribe()
    {
        presenter?.let { it.unSubscribe() }
        presenter = null
    }
}