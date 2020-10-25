package gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.ButterflyPhoto
import gr.jkapsouras.butterfliesofgreece.dto.Family
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_photos_table.view.*

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
//        ImageButterfly.image = UIImage(named: "Thumbnails/\(family.photo)", in: nil, compatibleWith: nil)
//        if  ImageButterfly.image == nil{
//            ImageButterfly.image = #imageLiteral(resourceName: "default")
//        }
//        ImageAdd.image = #imageLiteral(resourceName: "plusIcon").withRenderingMode(.alwaysTemplate)
    }

}