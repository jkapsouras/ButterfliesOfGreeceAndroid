package gr.jkapsouras.butterfliesofgreece.species

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import org.koin.android.ext.android.inject

class SpeciesFragment : BaseFragment<SpeciesPresenter>() {

    override val presenter: SpeciesPresenter by inject()

    override val layoutResource: Int
        get() = R.layout.species_fragment

    override fun initView(view: View): View {
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        return emptyList()
    }

}