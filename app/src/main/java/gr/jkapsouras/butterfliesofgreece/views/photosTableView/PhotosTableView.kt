package gr.jkapsouras.butterfliesofgreece.views.photosTableView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.dto.Family
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.PhotosTableAdapter
import gr.jkapsouras.butterfliesofgreece.views.photosTableView.adapter.ShowingStep
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.view_photos_table.view.*


class PhotosTableView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val source = PhotosTableAdapter()
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
        rv_photos_table.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_photos_table.adapter = source
        source.setFamilies(families)
        source.setShowingStep( ShowingStep.Families)
        source.notifyDataSetChanged()
    }
}