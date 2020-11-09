package gr.jkapsouras.butterfliesofgreece.fragments.families.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gr.jkapsouras.butterfliesofgreece.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.modal_photo_fragment.*
import kotlinx.android.synthetic.main.row_photos_table.view.*
import java.lang.Exception

class ModalPhotoFragment : Fragment(){

    lateinit var photos: List<String>
    private val layoutResource: Int
        get() = R.layout.modal_photo_fragment

    companion object {
        private const val ARG_POSITION = "ARG_POSITION"

        fun getInstance(position: Int, photos: List<String>) = ModalPhotoFragment().apply {
            arguments = bundleOf(ARG_POSITION to position)
            this.photos = photos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).toolbar.visibility = View.GONE
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as AppCompatActivity).toolbar.visibility = View.GONE

        button_close_modal.setOnClickListener {
            findNavController().navigateUp()
            (requireActivity() as AppCompatActivity).toolbar.visibility = View.VISIBLE
        }

        val position = requireArguments().getInt(ARG_POSITION)
        try {
            val iden = iv_modal_photo.resources.getIdentifier(
                "full_${photos[position]}",
                "drawable",
                iv_modal_photo.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(iv_modal_photo.resources, tmpiden, null)
                drawable.let {
                    iv_modal_photo.setImageResource(tmpiden)
                }
            }
        } catch (e: Exception) {
            iv_modal_photo.setImageDrawable(
                ContextCompat.getDrawable(
                    iv_modal_photo.context,
                    R.drawable.thumb_default
                )
            )
        }
    }
}