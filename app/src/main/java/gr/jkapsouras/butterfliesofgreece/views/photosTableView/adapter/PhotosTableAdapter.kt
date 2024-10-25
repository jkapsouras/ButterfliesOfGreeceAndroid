package gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.RowPhotosCollectionBinding
import com.sansoft.butterflies.databinding.RowPhotosTableBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import io.reactivex.rxjava3.subjects.PublishSubject

enum class ShowingStep {
    Families,
    Species,
    Photos,
    PhotosToPrint
}

class PhotosTableAdapter : RecyclerView.Adapter<PhotosTableViewCell>() {

    private lateinit var families: List<Family>
    private var species: List<Specie> = emptyList()
    private var photos: List<ButterflyPhoto> = emptyList()
    val emitter: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    private var showingStep = ShowingStep.Families
    private var fromSearch = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosTableViewCell {
        val binding = RowPhotosTableBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosTableViewCell(
            binding,
            emitter
        )
    }

    override fun onBindViewHolder(holder: PhotosTableViewCell, position: Int) {
        when (showingStep) {
            ShowingStep.Families ->
                holder.update(families[position], showingStep)
            ShowingStep.Species ->
                holder.update(species[position], showingStep, fromSearch)
            ShowingStep.Photos,
            ShowingStep.PhotosToPrint ->
                holder.update(photos[position], showingStep)
        }
    }

    override fun getItemCount(): Int {
        return when (showingStep) {
            ShowingStep.Families ->
                families.count()
            ShowingStep.Species ->
                species.count()
            ShowingStep.Photos,
            ShowingStep.PhotosToPrint ->
                photos.count()
        }
    }

    fun setFamilies(families: List<Family>) {
        this.families = families
    }

    fun setSpecies(species: List<Specie>) {
        this.species = species
    }

    fun setPhotos(photos: List<ButterflyPhoto>) {
        this.photos = photos
    }

    fun setShowingStep(showingStep: ShowingStep) {
        this.showingStep = showingStep
    }

    fun setFromSearch(fromSearch: Boolean) {
        this.fromSearch = fromSearch
    }
}