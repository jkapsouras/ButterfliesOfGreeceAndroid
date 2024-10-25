package gr.jkapsouras.butterfliesofgreece.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.SearchFragmentBinding
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.search.components.SearchHeaderComponent
import gr.jkapsouras.butterfliesofgreece.fragments.search.components.SearchResultComponent
import org.koin.android.ext.android.inject

class SearchFragment : BaseFragment<SearchPresenter>(){
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!
    override val presenter: SearchPresenter  by inject()
    private var searchResultComponent: SearchResultComponent? = null
    private var searchHeaderComponent: SearchHeaderComponent? = null

    override val layoutResource: Int
        get() = R.layout.search_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        searchResultComponent = SearchResultComponent(binding.viewPhotosTableSearch)
        searchHeaderComponent = SearchHeaderComponent((requireActivity() as MainActivity).binding.searchBar)
        return listOf(searchResultComponent!!, searchHeaderComponent!!)
    }

}