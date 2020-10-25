package gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_photos_table.view.*

enum class ShowingStep{
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
        return PhotosTableViewCell(LayoutInflater.from(parent.context).inflate(R.layout.row_photos_table, parent, false))
    }

    override fun onBindViewHolder(holder: PhotosTableViewCell, position: Int) {
        holder.update(families[position], emitter, showingStep)
    }

    override fun getItemCount(): Int {
        return when (showingStep) {
            ShowingStep.Families ->
                families.count()
            ShowingStep.Species ->
                species.count()
            ShowingStep.Photos ->
                photos.count()
            ShowingStep.PhotosToPrint ->
                photos.count()
        }
    }

    fun setFamilies(families: List<Family>){
        this.families = families
    }

    fun setSpecies(species: List<Specie>){
        this.species = species
    }

    fun setPhotos(photos: List<ButterflyPhoto>){
        this.photos = photos
    }

    fun setShowingStep(showingStep:ShowingStep){
        this.showingStep = showingStep
    }

    fun setFromSearch(fromSearch: Boolean){
        this.fromSearch = fromSearch
    }
}