package gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter

import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_photos_table.view.*
import java.lang.Exception

class PhotosTableViewCell(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var emitter:PublishSubject<UiEvent>? = null
    var familyId:Int = -1
    var specieId:Int = -1
    var photoId:Int = -1
    var photo: ButterflyPhoto? =null
    var showingStep:ShowingStep? = null

    fun update(family: Family, emitter: PublishSubject<UiEvent>, showingStep:ShowingStep){
        this.showingStep = showingStep
        this.emitter = emitter
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
//        itemView.label_photo_table_name.clipToOutline = true
//        ImageButterfly.image = UIImage(named: "Thumbnails/\(family.photo)", in: nil, compatibleWith: nil)
//        if  ImageButterfly.image == nil{
//            ImageButterfly.image = #imageLiteral(resourceName: "default")
//        }
//        ImageAdd.image = #imageLiteral(resourceName: "plusIcon").withRenderingMode(.alwaysTemplate)
    }

}