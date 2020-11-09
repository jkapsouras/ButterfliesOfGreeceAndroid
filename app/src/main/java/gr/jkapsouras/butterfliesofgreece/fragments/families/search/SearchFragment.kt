package gr.jkapsouras.butterfliesofgreece.fragments.families.search

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.components.PhotosCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.components.PhotosHeaderComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.components.PhotosTableComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.search.components.SearchHeaderComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.search.components.SearchResultComponent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.photos_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.android.ext.android.inject

class SearchFragment : BaseFragment<SearchPresenter>(){

    override val presenter: SearchPresenter  by inject()
    private var searchResultComponent: SearchResultComponent? = null
    private var searchHeaderComponent: SearchHeaderComponent? = null

    override val layoutResource: Int
        get() = R.layout.search_fragment

    override fun initView(view: View): View {
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        searchResultComponent = SearchResultComponent(view_photos_table_search)
        searchHeaderComponent = SearchHeaderComponent((requireActivity() as AppCompatActivity).search_bar)
        return listOf(searchResultComponent!!, searchHeaderComponent!!)
    }

}