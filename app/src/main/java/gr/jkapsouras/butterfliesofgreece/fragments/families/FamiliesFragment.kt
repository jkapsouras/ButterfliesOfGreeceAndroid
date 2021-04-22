package gr.jkapsouras.butterfliesofgreece.fragments.families

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.components.FamiliesCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.components.FamiliesTableComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.components.HeaderTableComponent
import kotlinx.android.synthetic.main.families_fragment.*
import org.koin.android.ext.android.inject

class FamiliesFragment : BaseFragment<FamiliesPresenter>(){

    override val presenter: FamiliesPresenter by inject()
    private var familiesTableComponent: FamiliesTableComponent? = null
    private var headerTableComponent: HeaderTableComponent? = null
    private var familiesCollectionComponent: FamiliesCollectionComponent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.families_fragment, container, false)
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        familiesTableComponent = FamiliesTableComponent(view_table_photos)
        familiesCollectionComponent = FamiliesCollectionComponent(view_collection_photos)
        headerTableComponent = HeaderTableComponent(view_header)
        return listOf(familiesTableComponent!!, headerTableComponent!!, familiesCollectionComponent!!)
    }

    override val layoutResource: Int
        get() = R.layout.families_fragment

    override fun initView(view: View): View {

        return view
    }
}