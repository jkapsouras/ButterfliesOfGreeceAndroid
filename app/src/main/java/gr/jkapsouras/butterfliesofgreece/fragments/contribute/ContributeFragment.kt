package gr.jkapsouras.butterfliesofgreece.fragments.contribute

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.FamiliesPresenter
import org.koin.android.ext.android.inject

class ContributeFragment : BaseFragment<FamiliesPresenter>(){

    override val presenter: FamiliesPresenter by inject()

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        return emptyList()
    }

    override val layoutResource: Int
        get() = R.layout.contribute_fragment

    override fun initView(view: View): View {
        return view
    }
}