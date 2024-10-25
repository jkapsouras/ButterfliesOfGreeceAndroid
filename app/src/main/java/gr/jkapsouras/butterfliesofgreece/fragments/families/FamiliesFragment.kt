package gr.jkapsouras.butterfliesofgreece.fragments.families

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.FamiliesFragmentBinding
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.components.FamiliesCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.components.FamiliesTableComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.components.HeaderTableComponent
import org.koin.android.ext.android.inject

class FamiliesFragment : BaseFragment<FamiliesPresenter>(){

    override val presenter: FamiliesPresenter by inject()
    private var _binding: FamiliesFragmentBinding? = null
    private val binding get() = _binding!!
    private var familiesTableComponent: FamiliesTableComponent? = null
    private var headerTableComponent: HeaderTableComponent? = null
    private var familiesCollectionComponent: FamiliesCollectionComponent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FamiliesFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        familiesTableComponent = FamiliesTableComponent(binding.viewTablePhotos)
        familiesCollectionComponent = FamiliesCollectionComponent(binding.viewCollectionPhotos)
        headerTableComponent = HeaderTableComponent(binding.viewHeader)
        return listOf(familiesTableComponent!!, headerTableComponent!!, familiesCollectionComponent!!)
    }

    override val layoutResource: Int
        get() = R.layout.families_fragment
}