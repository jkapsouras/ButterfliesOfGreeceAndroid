package gr.jkapsouras.butterfliesofgreece.fragments.families.photos

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.BaseFragment
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.components.PhotosCollectionComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.components.PhotosHeaderComponent
import gr.jkapsouras.butterfliesofgreece.fragments.families.photos.components.PhotosTableComponent
import kotlinx.android.synthetic.main.photos_fragment.*
import org.koin.android.ext.android.inject

class PhotosFragment : BaseFragment<PhotosPresenter>() {

    override val presenter: PhotosPresenter by inject()
    private var photosTableComponent: PhotosTableComponent? = null
    private var photosHeaderComponent: PhotosHeaderComponent? = null
    private var photosCollectionComponent:PhotosCollectionComponent? = null

    override val layoutResource: Int
        get() = R.layout.photos_fragment

    override fun initView(view: View): View {
        return view
    }

    override fun initializeComponents(constraintLayout: ConstraintLayout): List<UiComponent> {
        photosTableComponent = PhotosTableComponent(view_photos_table_photos)
        photosCollectionComponent = PhotosCollectionComponent(view_photos_collection_photos)
        photosHeaderComponent = PhotosHeaderComponent(view_photos_header)
        return listOf(photosTableComponent!!, photosCollectionComponent!!, photosHeaderComponent!!)
    }

}