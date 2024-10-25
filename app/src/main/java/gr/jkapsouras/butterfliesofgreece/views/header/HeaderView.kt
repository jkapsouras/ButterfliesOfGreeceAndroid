package gr.jkapsouras.butterfliesofgreece.views.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding4.view.clicks
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ViewHeaderBinding
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.views.header.uiEvents.HeaderViewEvents
import io.reactivex.rxjava3.core.Observable

class HeaderView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr){

    private var _binding: ViewHeaderBinding? = null
    private val binding get() = _binding!!
    lateinit var view : View
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        _binding = ViewHeaderBinding.inflate(LayoutInflater.from(context), this, true)
        view = binding.root
    }

    private fun viewEvents() : Observable<UiEvent>
    {
        return Observable.merge(binding.btChangeViewStyle.clicks().map{HeaderViewEvents.SwitchViewStyleClicked},
            binding.btSearchSpecies.clicks().map{HeaderViewEvents.SearchBarClicked},
            binding.btShowAddedPhotos.clicks().map{HeaderViewEvents.PrintPhotosClicked})
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
                binding.btChangeViewStyle.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.list_icon))
            ViewArrange.List ->
                binding.btChangeViewStyle.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.grid_icon))
        }
    }

    fun updateNumberOfPhotos(number:Int){
        binding.tvNumberViewHeader.text = number.toString()
    }

    fun updateTitle(title:String){
        binding.tvTitleViewHeader.text = title
    }
}