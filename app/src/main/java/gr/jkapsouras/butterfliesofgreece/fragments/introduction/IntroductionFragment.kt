package gr.jkapsouras.butterfliesofgreece.fragments.introduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gr.jkapsouras.butterfliesofgreece.R
import kotlinx.android.synthetic.main.endangered_fragment.*

class IntroductionFragment : Fragment(){

    val layoutResource: Int
        get() = R.layout.introduction_fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResource, container, false)
    }

}