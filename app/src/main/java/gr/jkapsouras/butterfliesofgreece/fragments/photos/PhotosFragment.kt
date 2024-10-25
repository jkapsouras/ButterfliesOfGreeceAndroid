package gr.jkapsouras.butterfliesofgreece.fragments.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.PhotosFragmentBinding
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.photos.components.PhotosCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.photos.components.PhotosHeaderComponent
import gr.jkapsouras.butterfliesofgreece.fragments.photos.components.PhotosTableComponent
import org.koin.android.ext.android.inject

class PhotosFragment : BaseFragment<PhotosPresenter>() {
    private var _binding: PhotosFragmentBinding? = null
    private val binding get() = _binding!!
    override val presenter: PhotosPresenter by inject()
    private var photosTableComponent: PhotosTableComponent? = null
    private var photosHeaderComponent: PhotosHeaderComponent? = null
    private var photosCollectionComponent:PhotosCollectionComponent? = null

    override val layoutResource: Int
        get() = R.layout.photos_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PhotosFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        photosTableComponent = PhotosTableComponent(binding.viewPhotosTablePhotos)
        photosCollectionComponent = PhotosCollectionComponent(binding.viewPhotosCollectionPhotos)
        photosHeaderComponent = PhotosHeaderComponent(binding.viewPhotosHeader)
        return listOf(photosTableComponent!!, photosCollectionComponent!!, photosHeaderComponent!!)
    }

}