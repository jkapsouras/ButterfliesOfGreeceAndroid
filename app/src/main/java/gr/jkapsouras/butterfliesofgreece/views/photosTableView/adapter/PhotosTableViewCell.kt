package gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter

import android.content.ContentValues
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.RowPhotosTableBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.fragments.families.uiEvents.FamilyEvents
import gr.jkapsouras.butterfliesofgreece.fragments.photos.uiEvents.PhotosEvents
import gr.jkapsouras.butterfliesofgreece.fragments.printToPdf.uiEvents.PrintToPdfEvents
import gr.jkapsouras.butterfliesofgreece.fragments.search.uiEvents.SearchEvents
import gr.jkapsouras.butterfliesofgreece.fragments.species.uiEvents.SpeciesEvents
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception

class PhotosTableViewCell(val binding: RowPhotosTableBinding, private val emitter: PublishSubject<UiEvent>) :
    RecyclerView.ViewHolder(binding.root) {

    var familyId: Int = -1
    var specieId: Int = -1
    var photoId: Int = -1
    var photo: ButterflyPhoto? = null
    var showingStep: ShowingStep? = null
    var fromSearch = false
    lateinit var specie: Specie

    init {
        itemView.setOnClickListener {
            when (showingStep) {
                ShowingStep.Families ->
                    emitter.onNext(FamilyEvents.FamilyClicked(familyId))
                ShowingStep.Species ->
                    if (fromSearch) {
                        Log.println(Log.INFO, "", "search clicked")
                        emitter.onNext(SearchEvents.SpecieClicked(specie = this.specie))
                    } else {
                        emitter.onNext(SpeciesEvents.SpecieClicked(specieId))
                    }
                ShowingStep.Photos,
                ShowingStep.PhotosToPrint ->
                    emitter.onNext(PhotosEvents.PhotoClicked(photoId))
                else ->
                    Log.d(ContentValues.TAG, "nothing")
            }
        }

        binding.ivAddImageRowTable.setOnClickListener {
            when (showingStep) {
                ShowingStep.Families ->
                    emitter.onNext(FamilyEvents.AddPhotosForPrintClicked(familyId))
                ShowingStep.Species ->
                    emitter.onNext(SpeciesEvents.AddPhotosForPrintClicked(specieId))
                ShowingStep.Photos ->
                    emitter.onNext(PhotosEvents.AddPhotoForPrintClicked(photoId))
                ShowingStep.PhotosToPrint ->
                    emitter.onNext(PrintToPdfEvents.Delete(photo = photo!!))
                else ->
                    Log.d(ContentValues.TAG, "nothing")
            }
            Log.d(Constraints.TAG, "field clicked")
        }
    }

    fun update(family: Family, showingStep: ShowingStep) {
        this.showingStep = showingStep
        binding.labelPhotoTableName.text = family.name
        familyId = family.id
        try {
            val iden = itemView.resources.getIdentifier(
                "thumb_${family.photo}",
                "drawable",
                itemView.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let {
                    binding.ivImageRowTable.setImageResource(tmpiden)
                }
            }
        } catch (e: Exception) {
            binding.ivImageRowTable.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.thumb_default
                )
            )
        }
    }

    fun update(specie: Specie, showingStep: ShowingStep, fromSearch: Boolean) {
        this.showingStep = showingStep
        this.fromSearch = fromSearch
        binding.labelPhotoTableName.text = specie.name
        specieId = specie.id
        this.specie = specie
        try {
            val iden = itemView.resources.getIdentifier(
                "thumb_${specie.imageTitle}",
                "drawable",
                itemView.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let {
                    binding.ivImageRowTable.setImageResource(tmpiden)
                }
            }
        } catch (e: Exception) {
            binding.ivImageRowTable.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.thumb_default
                )
            )
        }

        when {
            fromSearch -> binding.ivAddImageRowTable.visibility = View.INVISIBLE
            else -> binding.ivAddImageRowTable.visibility = View.VISIBLE
        }
    }

    fun update(photo: ButterflyPhoto, showingStep: ShowingStep) {
        this.showingStep = showingStep
        binding.labelPhotoTableName.text = photo.author
        photoId = photo.id
        this.photo = photo
        try {
            val iden = itemView.resources.getIdentifier(
                "thumb_${photo.source}",
                "drawable",
                itemView.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let {
                    binding.ivImageRowTable.setImageResource(tmpiden)
                }
            }
        } catch (e: Exception) {
            binding.ivImageRowTable.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.thumb_default
                )
            )
        }
        binding.ivAddImageRowTable.setImageResource(if (showingStep == ShowingStep.Photos)  R.drawable.plus_icon else R.drawable.minus_icon)
    }
}