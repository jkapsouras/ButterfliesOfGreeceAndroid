package gr.jkapsouras.butterfliesofgreece.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints.TAG
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import kotlinx.android.synthetic.main.main_menu_fragment.*

class MainMenuFragment : BaseFragment<MenuPresenter>() {

    var menuComponent:MenuComponent? = null

    companion object {
        fun newInstance() = MainMenuFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_menu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {

        menuComponent = MenuComponent(btn_field,btn_field,btn_field,btn_field,btn_field,btn_field,btn_field)

        return listOf(menuComponent!!)
    }
}