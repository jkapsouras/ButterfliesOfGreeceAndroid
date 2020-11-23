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
import gr.jkapsouras.butterfliesofgreece.fragments.families.viewStates.FamiliesViewViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.main.ViewStates.MenuViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.modal.viewStates.ModalViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.photos.viewStates.PhotosViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.viewStates.PrintToPdfViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.search.viewStates.SearchViewStates
import gr.jkapsouras.butterfliesofgreece.fragments.species.viewStates.SpeciesViewStates
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.FromFragment
import gr.jkapsouras.butterfliesofgreece.views.header.viewStates.HeaderViewViewStates
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
            is SearchViewStates -> {
                when (viewState){
                    is SearchViewStates.ToPhotosOfSpecie->
                        findNavController().navigate(R.id.navigate_to_species_from_search)
                }
            }
            is HeaderViewViewStates ->{
                when (viewState){
                    is HeaderViewViewStates.ToSearch -> {
                        when(viewState.from){
                            FromFragment.Families ->
                                findNavController().navigate(R.id.navigate_to_search_from_families)
                            FromFragment.Species ->
                                findNavController().navigate(R.id.navigate_to_search_from_species)
                            FromFragment.Photos ->
                                findNavController().navigate(R.id.navigate_to_search_from_photos)
                        }
                    }
                    is HeaderViewViewStates.ToPrintPhotos ->{
                        when(viewState.from){
                            FromFragment.Families ->
                                findNavController().navigate(R.id.navigate_to_print_from_families)
                            FromFragment.Species ->
                                findNavController().navigate(R.id.navigate_to_print_from_species)
                            FromFragment.Photos ->
                                findNavController().navigate(R.id.navigate_to_print_from_photos)
                        }
                    }
                }
            }
            is MenuViewStates -> {
                when (viewState) {
                    is MenuViewStates.ToField ->
                        findNavController().navigate(R.id.navigate_to_families)
                    is MenuViewStates.ToContribute ->
                        findNavController().navigate(R.id.navigate_to_contribute)
                    is MenuViewStates.ToEndangered ->
                        findNavController().navigate(R.id.navigate_to_endangered)
                    is MenuViewStates.ToIntroduction ->
                        findNavController().navigate(R.id.navigate_to_introduction)
                    is MenuViewStates.ToAbout ->
                        findNavController().navigate(R.id.navigate_to_about)
                    is MenuViewStates.ToLegal ->
                        findNavController().navigate(R.id.navigate_to_legal_fragment)
                    is MenuViewStates.ToRecognition ->
                        findNavController().navigate(R.id.navigate_to_recognition_fragment)
                }
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
            is PhotosViewStates -> {
                when(viewState){
                    is PhotosViewStates.ToPhoto ->
                        findNavController().navigate((R.id.navigate_to_modal))
                }
            }
            is ModalViewStates -> {
                when (viewState) {
                    is ModalViewStates.CloseModal ->
                        findNavController().navigateUp()
                }
            }
            is PrintToPdfViewStates ->{
                when(viewState){
                    is PrintToPdfViewStates.AllPhotosDeleted->
                        findNavController().navigateUp()
                    is PrintToPdfViewStates.ToPrintPreview ->
                        findNavController().navigate(R.id.navigate_to_previewer)
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