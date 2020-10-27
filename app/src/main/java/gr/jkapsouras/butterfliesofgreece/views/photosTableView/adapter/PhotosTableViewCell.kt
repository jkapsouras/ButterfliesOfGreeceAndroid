package gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter

import android.content.res.Resources
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.view.clicks
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.dto.Specie
import gr.jkapsouras.butterfliesofgreece.families.uiEvents.FamilyEvents
import gr.jkapsouras.butterfliesofgreece.main.events.MenuUiEvents
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_photos_table.view.*
import java.lang.Exception

class PhotosTableViewCell(itemView: View, private val emitter: PublishSubject<UiEvent>) : RecyclerView.ViewHolder(itemView) {

    var familyId:Int = -1
    var specieId:Int = -1
    var photoId:Int = -1
    var photo: ButterflyPhoto? =null
    var showingStep:ShowingStep? = null

    init{
         itemView.setOnClickListener {
            emitter.onNext(FamilyEvents.FamilyClicked(familyId))
            Log.d(Constraints.TAG, "field clicked")
        }
    }

    fun update(family: Family, showingStep:ShowingStep){
        this.showingStep = showingStep
        itemView.label_photo_table_name.text = family.name
        familyId = family.id
        try {
            val iden = itemView.resources.getIdentifier("thumb_${family.photo}", "drawable", itemView.context.packageName)
            iden.let {tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let{
                    itemView.iv_image_row_table.setImageResource(tmpiden)
                }
            }
        }
        catch (e:Exception){
            itemView.iv_image_row_table.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.thumb_default))
        }
    }

    fun update(specie: Specie, showingStep:ShowingStep, fromSearch: Boolean){
        this.showingStep = showingStep
        itemView.label_photo_table_name.text = specie.name
        specieId = specie.id
        try {
            val iden = itemView.resources.getIdentifier("thumb_${specie.imageTitle}", "drawable", itemView.context.packageName)
            iden.let {tmpiden ->
                val drawable = ResourcesCompat.getDrawable(itemView.resources, tmpiden, null)
                drawable.let{
                    itemView.iv_image_row_table.setImageResource(tmpiden)
                }
            }
        }
        catch (e:Exception){
            itemView.iv_image_row_table.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.thumb_default))
        }

        when {
            fromSearch -> itemView.iv_add_image_row_table.visibility =  View.INVISIBLE
            else -> itemView.iv_add_image_row_table.visibility =  View.VISIBLE
        }
    }
}