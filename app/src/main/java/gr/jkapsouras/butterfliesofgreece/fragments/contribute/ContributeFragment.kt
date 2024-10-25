package gr.jkapsouras.butterfliesofgreece.fragments.contribute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ContributeFragmentBinding
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.components.ContributeComponent
import org.koin.android.ext.android.inject

class ContributeFragment : BaseFragment<ContributePresenter>(){

    override val presenter: ContributePresenter by inject()
    private var _binding: ContributeFragmentBinding? = null
    private val binding get() = _binding!!
    private var contributeComponent: ContributeComponent? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContributeFragmentBinding.inflate(inflater, container, false)
        presenter.setActivity(requireActivity() as MainActivity)
        presenter.setView(binding.root)
        presenter.setBinding(binding.viewContribute.binding)
        presenter.setContext(requireContext())

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        contributeComponent = ContributeComponent(binding.viewContribute, requireActivity() as MainActivity)
        return listOf(contributeComponent!!)
    }

    override val layoutResource: Int
        get() = R.layout.contribute_fragment

    override fun onStop() {
        presenter.unSubscribe()
        super.onStop()
    }
}