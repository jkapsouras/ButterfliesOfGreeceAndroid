package gr.jkapsouras.butterfliesofgreece.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.MainMenuFragmentBinding
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import org.koin.android.ext.android.inject


class MainMenuFragment : BaseFragment<MenuPresenter>() {
    private var _binding: MainMenuFragmentBinding? = null
    private val binding get() = _binding!!
    override val layoutResource: Int
        get() = R.layout.main_menu_fragment
    override val presenter: MenuPresenter by inject()
    var menuComponent:MenuComponent? = null

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainMenuFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.VISIBLE
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {

        menuComponent = MenuComponent(
            binding.btnField,
            binding.btnContribute,
            binding.btnAbout,
            binding.btnIntroduction,
            binding.btnEndangered,
            binding.btnLegal,
            binding.btnRecognition
        )

        return listOf(menuComponent!!)
    }
}