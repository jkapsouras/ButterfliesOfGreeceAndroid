package gr.jkapsouras.butterfliesofgreece.fragments.introduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sansoft.butterflies.R

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