package gr.jkapsouras.butterfliesofgreece.fragments.recognition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.RecognitionFragmentBinding
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.recognition.component.RecognitionComponent
import org.koin.android.ext.android.inject

class RecognitionFragment : BaseFragment<RecognitionPresenter>(){
    private var _binding: RecognitionFragmentBinding? = null
    private val binding get() = _binding!!
    override val presenter: RecognitionPresenter by inject()
    private var recognitionComponent: RecognitionComponent? = null

    override val layoutResource: Int
        get() = R.layout.recognition_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecognitionFragmentBinding.inflate(inflater, container, false)
        presenter.setActivity(requireActivity() as MainActivity)

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        recognitionComponent = RecognitionComponent(
            requireActivity() as MainActivity,
            binding.buttonChooseImage,
            binding.buttonCaptureImage,
            binding.buttonLiveSession,
            binding.viewRecognitionOnlineOffline,
            binding.viewLiveSession,
            binding.viewOverlay
        )
        return listOf(recognitionComponent!!)
    }

}
