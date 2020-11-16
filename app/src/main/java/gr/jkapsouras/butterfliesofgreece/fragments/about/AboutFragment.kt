package gr.jkapsouras.butterfliesofgreece.fragments.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.extensions.ClickSpan
import kotlinx.android.synthetic.main.about_fragment.*

class AboutFragment : Fragment(){

    val layoutResource: Int
        get() = R.layout.about_fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ClickSpan.clickify(label_about_third, getString(R.string.about_link_first)) {
            val url = "https://yperdiavgeia.gr/decisions/downloadPdf/15680414"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        ClickSpan.clickify(label_about_third, getString(R.string.about_link_second)) {
            val url = "http://www.ypeka.gr/LinkClick.aspx?fileticket=FncrrCKwYAE%3D&tabid=918&language=el-GR"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}
