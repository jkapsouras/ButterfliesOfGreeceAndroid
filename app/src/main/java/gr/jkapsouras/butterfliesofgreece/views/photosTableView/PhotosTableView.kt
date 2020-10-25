package gr.jkapsouras.butterfliesofgreece.views.photosTableView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.Family
import io.reactivex.rxjava3.core.Observable


class PhotosTableView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var view : View
    val uiEvents: Observable<UiEvent> = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        view = LayoutInflater.from(context)
            .inflate(R.layout.view_photos_table, this)
    }

    fun show()
    {
        if (visibility == View.GONE)
            visibility = View.VISIBLE
    }

    fun hide()
    {
        if (visibility == View.VISIBLE)
            visibility = View.GONE
    }

    private fun viewEvents() : Observable<UiEvent>
    {
        return Observable.empty()
    }

    fun showFamilies(families: List<Family>){
//        source.setFamilies(families: families)
//        source.setShowingStep(showingStep: .families)
//        TablePhotos.separatorStyle = .none
//                TablePhotos.dataSource = source
//        TablePhotos.delegate = source
//        TablePhotos.reloadData()
    }
}