package gr.jkapsouras.butterfliesofgreece.views.photosCollectionView.adapter

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.families.uiEvents.FamilyEvents
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.ShowingStep
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_photos_collection.view.*

class PhotosCollectionViewCell(itemView: View, private val emitter: PublishSubject<UiEvent>) : RecyclerView.ViewHolder(itemView){

    var familyId:Int = -1
    var specieId:Int = -1
    var photoId:Int = -1
    var showingStep: ShowingStep? = null

    init{
        itemView.setOnClickListener {
            emitter.onNext(FamilyEvents.FamilyClicked(familyId))
            Log.d(Constraints.TAG, "field clicked")
        }
    }

    fun update(family: Family, showingStep: ShowingStep){
        this.showingStep = showingStep
        itemView.label_photo_collection_name.text = family.name
        familyId = family.id
        try {
            val iden = itemView.resources.getIdentifier(
                "thumb_${family.photo}",
                "drawable",
                itemView.context.packageName
            )
            iden.let { tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let{
                    itemView.iv_image_row_collection.setImageResource(tmpiden)
                }
            }
        }
        catch (e: Exception){
            itemView.iv_image_row_collection.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.thumb_default
                )
            )
        }

        itemView.cardView_image_row_collection.post {
            val w = itemView.cardView_image_row_collection.width
            itemView.cardView_image_row_collection.apply {
                radius = (w/2).toDp(itemView.context).toFloat()
            }
        }
    }

    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()
}