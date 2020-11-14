package gr.jkapsouras.butterfliesofgreece.views.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding4.view.clicks
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.views.header.uiEvents.HeaderViewEvents
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.view_header.view.*

class HeaderView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr){

    lateinit var view : View
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        view = LayoutInflater.from(context)
            .inflate(R.layout.view_header, this)
    }

    private fun viewEvents() : Observable<UiEvent>
    {
        return Observable.merge(bt_change_view_style.clicks().map{HeaderViewEvents.SwitchViewStyleClicked},
            bt_search_species.clicks().map{HeaderViewEvents.SearchBarClicked},
            bt_show_added_photos.clicks().map{HeaderViewEvents.PrintPhotosClicked})
    }

    fun show()
    {
        if (visibility == View.INVISIBLE)
            visibility = View.VISIBLE
    }

    fun hide()
    {
        if (visibility == View.VISIBLE)
            visibility = View.INVISIBLE
    }

    fun changeViewForViewArrange(viewArrange: ViewArrange){
        when (viewArrange) {
            ViewArrange.Grid ->
                bt_change_view_style.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.list_icon))
            ViewArrange.List ->
                bt_change_view_style.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.grid_icon))
        }
    }

    fun updateNumberOfPhotos(number:Int){
        tv_number_view_header.text = number.toString()
    }

    fun updateTitle(title:String){
        tv_title_view_header.text = title
    }
}