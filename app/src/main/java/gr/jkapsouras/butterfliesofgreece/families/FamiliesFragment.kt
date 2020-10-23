package gr.jkapsouras.butterfliesofgreece.families

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.ext.getScopeName
import java.lang.reflect.Type

class FamiliesFragment :BaseFragment<FamiliesPresenter>(){

    override val presenter: FamiliesPresenter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.families_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        return emptyList()
    }

    override val layoutResource: Int
        get() = R.layout.families_fragment

    override fun initView(view: View): View {
        return view
    }
}