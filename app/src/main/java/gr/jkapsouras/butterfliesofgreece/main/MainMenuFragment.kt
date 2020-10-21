package gr.jkapsouras.butterfliesofgreece.main

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_menu_fragment.*
import org.koin.android.ext.android.inject


class MainMenuFragment : BaseFragment<MenuPresenter>() {

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
    ): View? {

        return inflater.inflate(R.layout.main_menu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).toolbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).toolbar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).toolbar.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as AppCompatActivity).toolbar.visibility = View.VISIBLE
    }

    override fun initView(view: View): View {
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {

        menuComponent = MenuComponent(
            btn_field,
            btn_field,
            btn_field,
            btn_field,
            btn_field,
            btn_field,
            btn_field
        )

        return listOf(menuComponent!!)
    }
}