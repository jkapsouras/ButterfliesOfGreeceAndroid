package gr.jkapsouras.butterfliesofgreece.views.photosTableView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ViewPhotosTableBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.PhotosTableAdapter
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.ShowingStep
import io.reactivex.rxjava3.core.Observable


class PhotosTableView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    private var _binding: ViewPhotosTableBinding? = null
    private val binding get() = _binding!!
    private val source = PhotosTableAdapter()
    lateinit var view : View
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        _binding = ViewPhotosTableBinding.inflate(LayoutInflater.from(context), this, true)
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
        binding.rvPhotosTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvPhotosTable.adapter = source
        source.setFamilies(families)
        source.setShowingStep( ShowingStep.Families)
        source.notifyDataSetChanged()
    }

    fun showSpecies(species: List<Specie>, fromSearch: Boolean){
        binding.rvPhotosTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvPhotosTable.adapter = source
        source.setSpecies(species)
        source.setShowingStep( ShowingStep.Species)
        source.setFromSearch(fromSearch)
        source.notifyDataSetChanged()
    }

    fun showPhotos(photos: List<ButterflyPhoto>){
        binding.rvPhotosTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvPhotosTable.adapter = source
        source.setPhotos(photos)
        source.setShowingStep( ShowingStep.Photos)
        source.notifyDataSetChanged()
    }

    fun showPhotosToPrint(photos: List<ButterflyPhoto>){
        binding.rvPhotosTable.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvPhotosTable.adapter = source
        source.setPhotos(photos)
        source.setShowingStep( ShowingStep.PhotosToPrint)
        source.notifyDataSetChanged()
    }
}