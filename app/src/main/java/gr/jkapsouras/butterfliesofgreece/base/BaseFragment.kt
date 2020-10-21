package gr.jkapsouras.butterfliesofgreece.base

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.families.FamiliesFragment
import gr.jkapsouras.butterfliesofgreece.families.FamiliesPresenter
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.families_fragment.view.*
import org.koin.android.ext.android.inject


abstract  class BaseFragment<P : BasePresenter> : Fragment(){

//    private val _presenter: BasePresenter by inject()
//    private var presenter: P? = null
//        get() {
//            return _presenter as P?
//        }
    abstract val presenter: P
    lateinit var events:Observable<UiEvent>
    lateinit var components:List<UiComponent>

    protected abstract val layoutResource:Int
    internal abstract fun  initView(view:View) : View
    abstract fun  initializeComponents(constraintLayout: ConstraintLayout):List<UiComponent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView(inflater.inflate(layoutResource, container, false));
    }

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

    override fun onStop() {
        super.onStop()
        unSubscribe()
    }

    private fun transitionStateReceived(viewState:ViewState){
        Log.d(TAG, "transitionStateReceived: ${viewState.toString()}")
        findNavController().navigate(R.id.navigate_to_families)
    }

    fun unSubscribe()
    {
        presenter?.let { it.unSubscribe() }
//        presenter = null
    }
}