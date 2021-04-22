package gr.jkapsouras.butterfliesofgreece.fragments.species

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.species.components.SpeciesCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.species.components.SpeciesHeaderComponent
import gr.jkapsouras.butterfliesofgreece.fragments.species.components.SpeciesTableComponent
import kotlinx.android.synthetic.main.species_fragment.*
import org.koin.android.ext.android.inject

class SpeciesFragment : BaseFragment<SpeciesPresenter>() {

    override val presenter: SpeciesPresenter by inject()
    private var speciesTableComponent: SpeciesTableComponent? = null
    private var headerTableComponent: SpeciesHeaderComponent? = null
    private var speciesCollectionComponent: SpeciesCollectionComponent? = null

    override val layoutResource: Int
        get() = R.layout.species_fragment

    override fun initView(view: View): View {
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        speciesTableComponent = SpeciesTableComponent(view_spcecies_table_photos)
        speciesCollectionComponent = SpeciesCollectionComponent(view_spcecies_collection_photos)
        headerTableComponent = SpeciesHeaderComponent(view_species_header)
        return listOf(speciesTableComponent!!, headerTableComponent!!, speciesCollectionComponent!!)
    }

}