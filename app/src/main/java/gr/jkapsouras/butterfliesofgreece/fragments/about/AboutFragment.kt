package gr.jkapsouras.butterfliesofgreece.fragments.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.AboutFragmentBinding
import gr.jkapsouras.butterfliesofgreece.extensions.ClickSpan

class AboutFragment : Fragment(){

    private var _binding: AboutFragmentBinding? = null
    private val binding get() = _binding!!
    val layoutResource: Int
        get() = R.layout.about_fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AboutFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ClickSpan.clickify(binding.labelAboutThird, getString(R.string.about_link_first)) {
            val url = "https://yperdiavgeia.gr/decisions/downloadPdf/15680414"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        ClickSpan.clickify(binding.labelAboutThird, getString(R.string.about_link_second)) {
            val url = "http://www.ypeka.gr/LinkClick.aspx?fileticket=FncrrCKwYAE%3D&tabid=918&language=el-GR"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}
