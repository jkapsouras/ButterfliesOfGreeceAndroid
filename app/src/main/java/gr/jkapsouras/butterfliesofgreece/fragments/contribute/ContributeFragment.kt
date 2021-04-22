package gr.jkapsouras.butterfliesofgreece.fragments.contribute

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.components.ContributeComponent
import kotlinx.android.synthetic.main.contribute_fragment.*
import org.koin.android.ext.android.inject

class ContributeFragment : BaseFragment<ContributePresenter>(){

    override val presenter: ContributePresenter by inject()
    private var contributeComponent: ContributeComponent? = null

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        contributeComponent = ContributeComponent(view_contribute, requireActivity() as MainActivity)
        return listOf(contributeComponent!!)
    }

    override val layoutResource: Int
        get() = R.layout.contribute_fragment

    override fun initView(view: View): View {
        presenter.setActivity(requireActivity() as MainActivity)
        presenter.setView(view)
        presenter.setContext(requireContext())
        return view
    }

    override fun onStop() {
        presenter.unSubscribe()
        super.onStop()
    }
}