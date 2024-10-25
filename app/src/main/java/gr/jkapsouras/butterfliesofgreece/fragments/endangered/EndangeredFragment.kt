package gr.jkapsouras.butterfliesofgreece.fragments.endangered

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.EndangeredFragmentBinding

class EndangeredFragment : Fragment() {
    private var _binding: EndangeredFragmentBinding? = null
    private val binding get() = _binding!!

     val layoutResource: Int
        get() = R.layout.endangered_fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EndangeredFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val stream = context?.assets?.open("species.pdf")
        binding.pdfViewEndangered.fromStream(stream!!)
            .password(null) // if password protected, then write password
            .defaultPage(0) // set the default page to open
            .enableSwipe(true)
            .spacing(8)
            .load()
    }

}