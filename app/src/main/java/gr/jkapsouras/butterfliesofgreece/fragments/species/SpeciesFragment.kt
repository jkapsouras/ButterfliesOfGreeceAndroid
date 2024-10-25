package gr.jkapsouras.butterfliesofgreece.fragments.species

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.SpeciesFragmentBinding
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.species.components.SpeciesCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.species.components.SpeciesHeaderComponent
import gr.jkapsouras.butterfliesofgreece.fragments.species.components.SpeciesTableComponent
import org.koin.android.ext.android.inject

class SpeciesFragment : BaseFragment<SpeciesPresenter>() {
    private var _binding: SpeciesFragmentBinding? = null
    private val binding get() = _binding!!
    override val presenter: SpeciesPresenter by inject()
    private var speciesTableComponent: SpeciesTableComponent? = null
    private var headerTableComponent: SpeciesHeaderComponent? = null
    private var speciesCollectionComponent: SpeciesCollectionComponent? = null

    override val layoutResource: Int
        get() = R.layout.species_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SpeciesFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        speciesTableComponent = SpeciesTableComponent(binding.viewSpceciesTablePhotos)
        speciesCollectionComponent = SpeciesCollectionComponent(binding.viewSpceciesCollectionPhotos)
        headerTableComponent = SpeciesHeaderComponent(binding.viewSpeciesHeader)
        return listOf(speciesTableComponent!!, headerTableComponent!!, speciesCollectionComponent!!)
    }

}