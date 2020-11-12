package gr.jkapsouras.butterfliesofgreece.views.contributeView

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.LocaleData
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.R
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.uiEvents.ContributeEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_contribute.view.*
import java.time.LocalDate
import java.util.*

class ContributeView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val emitter = PublishSubject.create<UiEvent>()
    lateinit var view : View
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        view = LayoutInflater.from(context)
            .inflate(R.layout.view_contribute, this)
    }

    private fun viewEvents() : Observable<UiEvent>
    {


        label_date.setOnClickListener {
            emitter.onNext(ContributeEvents.TextDateClicked)
        }

        edit_date.setOnClickListener {
            emitter.onNext(ContributeEvents.TextDateClicked)
        }

        edit_photo_name.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextNameSet(edit_photo_name.text.toString()))
        }

        edit_altitude.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextAltitudeSet(edit_altitude.text.toString()))
        }

        edit_place.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextPlaceSet(edit_place.text.toString()))
        }

        edit_stage.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextStateSet(edit_stage.text.toString()))
        }

        edit_genus.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextGenusSpeciesSet(edit_genus.text.toString()))
        }

        edit_name_species.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextNameSpeciesSet(edit_name_species.text.toString()))
        }

        edit_comments.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextCommentsSet(edit_comments.text.toString()))
        }

        return emitter
    }

    fun showDatePicker(){
//        ViewDate.alpha = 1

        val tmpDialog = DatePickerDialog(view.context)
        tmpDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            emitter.onNext(ContributeEvents.ButtonDoneClicked(LocalDate.of(year, month, dayOfMonth)))
        }
        tmpDialog.show()
    }

    fun hideDatePicker(){
//        ViewDate.alpha = 0
    }

    fun setDate(date:String){
       edit_date.setText(date)
    }

    fun setLocation(latitude:String, longitude:String){
        edit_longitude.setText(longitude)
        edit_latitude.setText(latitude)
        edit_longitude.isClickable = false
        edit_latitude.isClickable = false
    }

    fun locationError(controller: MainActivity){
        edit_longitude.isClickable = true
        edit_latitude.isClickable = true
//        let alert = UIAlertController(title: Translations.LocationErrorTitle, message: Translations.LocationErrorMessage, preferredStyle: .alert)
//        alert.addAction(UIAlertAction(title: Translations.Ok, style: .default, handler: nil))
//        controller.present(alert, animated: true)
    }

    fun openPromptToSettingsDialog(controller: MainActivity){
//        let alert = UIAlertController(title: Translations.NoLocationRights, message: Translations.NoLocationRightsMessage, preferredStyle: .alert)
//        alert.addAction(UIAlertAction(title: Translations.OpenSettings, style: .default, handler: { action in
//                UIApplication.shared.open(URL(string:UIApplication.openSettingsURLString)!)
//        }))
//        controller.present(alert, animated: true)
    }

    fun showItem(added: Boolean, controller: MainActivity){
//        if added{
//            let alert = UIAlertController(title: Translations.Contribution, message: Translations.ContributionAdded, preferredStyle: .alert)
//            alert.addAction(UIAlertAction(title: Translations.Ok, style: .default, handler: nil))
//            controller.present(alert, animated: true)
//        }
//        else{
//            let alert = UIAlertController(title: Translations.Contribution, message: Translations.ContributionNotAdded, preferredStyle: .alert)
//            alert.addAction(UIAlertAction(title: Translations.Ok, style: .default, handler: nil))
//            controller.present(alert, animated: true)
//        }
    }
}