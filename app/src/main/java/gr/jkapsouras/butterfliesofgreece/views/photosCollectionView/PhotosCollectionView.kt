package gr.jkapsouras.butterfliesofgreece.views.photosCollectionView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.sansoft.butterflies.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.views.photosCollectionView.adapter.PhotosCollectionAdapter
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.ShowingStep
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.view_photos_collection.view.*

class PhotosCollectionView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val source = PhotosCollectionAdapter()
    lateinit var view: View
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        view = LayoutInflater.from(context)
            .inflate(R.layout.view_photos_collection, this)
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
        rv_photos_collection.layoutManager = GridLayoutManager( rv_photos_collection.context, 2, GridLayoutManager.VERTICAL, false)
        rv_photos_collection.adapter = source
        source.setFamilies(families)
        source.setShowingStep( ShowingStep.Families)
        source.notifyDataSetChanged()
    }

    fun showSpecies(species: List<Specie>){
        rv_photos_collection.layoutManager = GridLayoutManager( rv_photos_collection.context, 2, GridLayoutManager.VERTICAL, false)
        rv_photos_collection.adapter = source
        source.setSpecies(species)
        source.setShowingStep( ShowingStep.Species)
        source.notifyDataSetChanged()
    }

    fun showPhotos(photos: List<ButterflyPhoto>){
        rv_photos_collection.layoutManager = GridLayoutManager( rv_photos_collection.context, 2, GridLayoutManager.VERTICAL, false)
        rv_photos_collection.adapter = source
        source.setPhotos(photos)
        source.setShowingStep(ShowingStep.Photos)
        source.notifyDataSetChanged()
    }
}