package gr.jkapsouras.butterfliesofgreece.base

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.fragments.families.families.viewStates.FamiliesViewViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.families.main.ViewStates.MenuViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.families.species.viewStates.SpeciesViewStates
import io.reactivex.rxjava3.core.Observable


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
        return initView(inflater.inflate(layoutResource, container, false))
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

        val state = presenter.subscribe(events).publish()

        components.forEach{component->
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
        when (viewState) {
            is MenuViewStates -> {
                findNavController().navigate(R.id.navigate_to_families)
            }
            is FamiliesViewViewStates ->{
                when (viewState) {
                    is FamiliesViewViewStates.ToSpecies ->
                        findNavController().navigate(R.id.navigate_to_species)
                }
            }
            is SpeciesViewStates ->{
                when(viewState){
                    is SpeciesViewStates.ToPhotos ->
                        findNavController().navigate((R.id.navigate_to_photos))
                }
            }
        }
    }

    private fun unSubscribe()
    {
       presenter.unSubscribe()
//        presenter = null
    }
}