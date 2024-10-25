package gr.jkapsouras.butterfliesofgreece.views.contributeView

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.sansoft.butterflies.R
import com.sansoft.butterflies.databinding.ViewContributeBinding
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.uiEvents.ContributeEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.time.LocalDate


class ContributeView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private val emitter = PublishSubject.create<UiEvent>()
    lateinit var view : View
    private var _binding: ViewContributeBinding? = null
    val binding get() = _binding!!
    lateinit var dialog: AlertDialog
    val uiEvents: Observable<UiEvent>
        get() = viewEvents()

    init {
        initialize(context)
    }

    private fun initialize(context: Context) {
        _binding = ViewContributeBinding.inflate(LayoutInflater.from(context), this, true)
        view = binding.root
    }

    private fun viewEvents() : Observable<UiEvent>
    {
        binding.labelDate.setOnClickListener {
            emitter.onNext(ContributeEvents.TextDateClicked)
        }

       binding.editDate.setOnClickListener {
            emitter.onNext(ContributeEvents.TextDateClicked)
        }

       binding.editPhotoName.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextNameSet(binding.editPhotoName.text.toString()))
        }

        binding.editAltitude.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextAltitudeSet(binding.editAltitude.text.toString()))
        }

        binding.editPlace.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextPlaceSet(binding.editPlace.text.toString()))
        }

        binding.editStage.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextStateSet(binding.editStage.text.toString()))
        }

        binding.editGenus.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextGenusSpeciesSet(binding.editGenus.text.toString()))
        }

        binding.editNameSpecies.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextNameSpeciesSet(binding.editNameSpecies.text.toString()))
        }

        binding.editComments.addTextChangedListener  {
            emitter.onNext(ContributeEvents.TextCommentsSet(binding.editComments.text.toString()))
        }

        binding.buttonAddContribute.setOnClickListener {
            emitter.onNext(ContributeEvents.AddClicked)
        }

       binding.buttonShareContribute.setOnClickListener {
            emitter.onNext(ContributeEvents.ExportClicked)
        }

       binding.buttonPrint.setOnClickListener {
            emitter.onNext(ContributeEvents.SharePdf)
        }

        return emitter
    }

    fun showDatePicker(){
//        ViewDate.alpha = 1

        val tmpDialog = DatePickerDialog(view.context)
        tmpDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            emitter.onNext(
                ContributeEvents.ButtonDoneClicked(
                    LocalDate.of(
                        year,
                        month,
                        dayOfMonth
                    )
                )
            )
        }
        tmpDialog.show()
    }

    fun hideDatePicker(){
//        ViewDate.alpha = 0
    }

    fun setDate(date: String){
       binding.editDate.setText(date)
    }

    fun setLocation(latitude: String, longitude: String){
       binding.editLongitude.setText(longitude)
       binding.editLatitude.setText(latitude)
        binding.editLongitude.isClickable = false
        binding.editLatitude.isClickable = false
    }

    fun locationError(controller: MainActivity){
        binding.editLongitude.isClickable = true
        binding.editLatitude.isClickable = true
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
        if (added){

            val builder = AlertDialog.Builder(view.context)

            with(builder)
            {
                setTitle(view.context.getString(R.string.contribute))
                setMessage(view.context.getString(R.string.contribution_added))
                setCancelable(false)
                setPositiveButton("ok") { _, _ ->

                }
                show()
            }
        }
        else{
            val builder = AlertDialog.Builder(view.context)
            with(builder)
            {
                setTitle(view.context.getString(R.string.contribute))
                setMessage(view.context.getString(R.string.contribution_not_added))
                setCancelable(false)
                setPositiveButton("ok") { _, _ ->

                }
                show()
            }
        }
    }
}