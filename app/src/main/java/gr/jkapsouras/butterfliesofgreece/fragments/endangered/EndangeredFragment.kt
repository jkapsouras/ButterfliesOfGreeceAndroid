package gr.jkapsouras.butterfliesofgreece.fragments.endangered

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansoft.butterflies.R
import kotlinx.android.synthetic.main.endangered_fragment.*

class EndangeredFragment : Fragment() {

     val layoutResource: Int
        get() = R.layout.endangered_fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        var stream = context?.assets?.open("species.pdf")
        pdfView_endangered.fromStream(stream!!)
            .password(null) // if password protected, then write password
            .defaultPage(0) // set the default page to open
            .enableSwipe(true)
            .spacing(8)
            .load()
    }

}