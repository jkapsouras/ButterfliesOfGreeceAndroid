package gr.jkapsouras.butterfliesofgreece.views.photosCollectionView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ViewPhotosCollectionBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.views.photosCollectionView.adapter.PhotosCollectionAdapter
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.ShowingStep
import io.reactivex.rxjava3.core.Observable

class PhotosCollectionView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    private var _binding: ViewPhotosCollectionBinding? = null
    private val binding get() = _binding!!
    private val source = PhotosCollectionAdapter()
    lateinit var view: View
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        _binding = ViewPhotosCollectionBinding.inflate(LayoutInflater.from(context), this, true)
        view = binding.root
    }

    fun show()
    {
        if (visibility == View.INVISIBLE)
            visibility = View.VISIBLE
    }

    fun hide()
    {
        if (visibility == View.VISIBLE)
            visibility = View.INVISIBLE
    }

    private fun viewEvents() : Observable<UiEvent>
    {
        return source.emitter
    }

    fun showFamilies(families: List<Family>){
        binding.rvPhotosCollection.layoutManager = GridLayoutManager( binding.rvPhotosCollection.context, 2, GridLayoutManager.VERTICAL, false)
        binding.rvPhotosCollection.adapter = source
        source.setFamilies(families)
        source.setShowingStep( ShowingStep.Families)
        source.notifyDataSetChanged()
    }

    fun showSpecies(species: List<Specie>){
        binding.rvPhotosCollection.layoutManager = GridLayoutManager( binding.rvPhotosCollection.context, 2, GridLayoutManager.VERTICAL, false)
        binding.rvPhotosCollection.adapter = source
        source.setSpecies(species)
        source.setShowingStep( ShowingStep.Species)
        source.notifyDataSetChanged()
    }

    fun showPhotos(photos: List<ButterflyPhoto>){
        binding.rvPhotosCollection.layoutManager = GridLayoutManager( binding.rvPhotosCollection.context, 2, GridLayoutManager.VERTICAL, false)
        binding.rvPhotosCollection.adapter = source
        source.setPhotos(photos)
        source.setShowingStep(ShowingStep.Photos)
        source.notifyDataSetChanged()
    }
}