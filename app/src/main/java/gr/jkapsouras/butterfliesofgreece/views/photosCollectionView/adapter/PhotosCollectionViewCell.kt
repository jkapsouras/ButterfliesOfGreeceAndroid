package gr.jkapsouras.butterfliesofgreece.views.photosCollectionView.adapter

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.RowPhotosCollectionBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.fragments.families.uiEvents.FamilyEvents
import gr.jkapsouras.butterfliesofgreece.fragments.photos.uiEvents.PhotosEvents
import gr.jkapsouras.butterfliesofgreece.fragments.species.uiEvents.SpeciesEvents
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.ShowingStep
import io.reactivex.rxjava3.subjects.PublishSubject

class PhotosCollectionViewCell(val binding: RowPhotosCollectionBinding, private val emitter: PublishSubject<UiEvent>) : RecyclerView.ViewHolder(binding.root){

    var familyId:Int = -1
    var specieId:Int = -1
    var photoId:Int = -1
    var showingStep: ShowingStep? = null

    init{
        itemView.setOnClickListener {
            when(showingStep)
            {
                ShowingStep.Families ->
                    emitter.onNext(FamilyEvents.FamilyClicked(familyId))
                ShowingStep.Species ->
                          emitter.onNext(SpeciesEvents.SpecieClicked(specieId))
                ShowingStep.Photos ->
                    emitter.onNext(PhotosEvents.PhotoClicked(photoId))
                else ->
                    Log.d(ContentValues.TAG, "nothing")
            }
            Log.d(Constraints.TAG, "field clicked")
        }

        binding.imageAddCollection.setOnClickListener {
            when (showingStep) {
                ShowingStep.Families ->
                    emitter.onNext(FamilyEvents.AddPhotosForPrintClicked(familyId))
                ShowingStep.Species ->
                    emitter.onNext(SpeciesEvents.AddPhotosForPrintClicked(specieId))
                ShowingStep.Photos ->
                    emitter.onNext(PhotosEvents.AddPhotoForPrintClicked(photoId))
//                     case .photosToPrint:
//                     if let photo = photo{
//                         emitter.onNext(PrintToPdfEvents.delete(photo: photo))
//                     }
                else ->
                    Log.d(ContentValues.TAG, "nothing")
            }
        }
    }

    fun update(family: Family, showingStep: ShowingStep){
        this.showingStep = showingStep
        binding.labelPhotoCollectionName.text = family.name
        familyId = family.id
        try {
            val iden = itemView.resources.getIdentifier(
                "thumb_big_${family.photo}",
                "drawable",
                itemView.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let{
                    binding.ivImageRowCollection.setImageResource(tmpiden)
                }
            }
        }
        catch (e: Exception){
            binding.ivImageRowCollection.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.thumb_default
                )
            )
        }

        binding.cardViewImageRowCollection.post {
            val w = binding.cardViewImageRowCollection.width
            binding.cardViewImageRowCollection.apply {
                radius = (w/2).toDp(itemView.context).toFloat()
            }
        }
    }

    fun update(specie: Specie, showingStep: ShowingStep){
        this.showingStep = showingStep
        binding.labelPhotoCollectionName.text = specie.name
        specieId = specie.id
        try {
            val iden = itemView.resources.getIdentifier(
                "thumb_big_${specie.imageTitle}",
                "drawable",
                itemView.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let{
                    binding.ivImageRowCollection.setImageResource(tmpiden)
                }
            }
        }
        catch (e: Exception){
            binding.ivImageRowCollection.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.thumb_default
                )
            )
        }

        binding.cardViewImageRowCollection.post {
            val w = binding.cardViewImageRowCollection.width
            binding.cardViewImageRowCollection.apply {
                radius = (w/2).toDp(itemView.context).toFloat()
            }
        }
    }

    fun update(photo: ButterflyPhoto, showingStep:ShowingStep){
        this.showingStep = showingStep
        binding.labelPhotoCollectionName.text = photo.author
        photoId = photo.id
        try {
            val iden = itemView.resources.getIdentifier(
                "thumb_big_${photo.source}",
                "drawable",
                itemView.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let{
                    binding.ivImageRowCollection.setImageResource(tmpiden)
                }
            }
        }
        catch (e: Exception){
            binding.ivImageRowCollection.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.thumb_default
                )
            )
        }

        binding.cardViewImageRowCollection.post {
            val w = binding.cardViewImageRowCollection.width
            binding.cardViewImageRowCollection.apply {
                radius = (w/2).toDp(itemView.context).toFloat()
            }
        }
    }

    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()
}