package gr.jkapsouras.butterfliesofgreece.fragments.modal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ModalPhotoFragmentBinding
import gr.jkapsouras.butterfliesofgreece.MainActivity
import java.lang.Exception

class ModalPhotoFragment : Fragment(){
    private var _binding: ModalPhotoFragmentBinding? = null
    private val binding get() = _binding!!

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
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ModalPhotoFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).binding.toolbar.visibility = View.GONE

         binding.buttonCloseModal.setOnClickListener {
            findNavController().navigateUp()
            (requireActivity() as MainActivity).binding.toolbar.visibility = View.VISIBLE
        }

        val position = requireArguments().getInt(ARG_POSITION)
        try {
            val iden = binding.ivModalPhoto.resources.getIdentifier(
                "thumb_big_${photos[position]}",
                "drawable",
                binding.ivModalPhoto.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(binding.ivModalPhoto.resources, tmpiden, null)
                drawable.let {
                    binding.ivModalPhoto.setImageResource(tmpiden)
                }
            }
        } catch (e: Exception) {
            binding.ivModalPhoto.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivModalPhoto.context,
                    R.drawable.thumb_default
                )
            )
        }
    }
}