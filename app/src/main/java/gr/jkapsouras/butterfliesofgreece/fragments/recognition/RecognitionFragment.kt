package gr.jkapsouras.butterfliesofgreece.fragments.recognition

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.component.RecognitionComponent
import kotlinx.android.synthetic.main.recognition_fragment.*
import org.koin.android.ext.android.inject

class RecognitionFragment : BaseFragment<RecognitionPresenter>(){

    override val presenter: RecognitionPresenter by inject()
    private var recognitionComponent: RecognitionComponent? = null

    override val layoutResource: Int
        get() = R.layout.recognition_fragment

    override fun initView(view: View): View {
        presenter.setActivity(requireActivity() as MainActivity)
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        recognitionComponent = RecognitionComponent(requireActivity() as MainActivity, button_choose_image, button_capture_image, button_live_session, view_recognition_online_offline, view_live_session)
        return listOf(recognitionComponent!!)
    }

}
