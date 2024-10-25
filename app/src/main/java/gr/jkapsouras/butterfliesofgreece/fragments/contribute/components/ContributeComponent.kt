package gr.jkapsouras.butterfliesofgreece.fragments.contribute.components

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import android.view.View
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.uiEvents.ContributeEvents
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.viewStates.ContributeViewStates
import gr.jkapsouras.butterfliesofgreece.managers.LocationManager.Companion.TAG
import gr.jkapsouras.butterfliesofgreece.managers.PdfManager
import gr.jkapsouras.butterfliesofgreece.views.contributeView.ContributeView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class ContributeComponent(private val view: ContributeView,
                          private  val owner: MainActivity) : UiComponent {
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(view.uiEvents, event)
    }

    @SuppressLint("RestrictedApi")
    override  fun renderViewState(viewState: ViewState) {
        if (viewState is ContributeViewStates) {
            when (viewState) {
                ContributeViewStates.ShowDatePicker ->
                    view.showDatePicker()
                ContributeViewStates.HideDatePicker ->
                    view.hideDatePicker()
                is ContributeViewStates.SetDate ->
                    view.setDate(date = viewState.date)
                is ContributeViewStates.ShowLocation ->
                    view.setLocation(latitude = viewState.latitude, longitude = viewState.longitude)
                ContributeViewStates.ShowSettingsDialog ->
                    view.openPromptToSettingsDialog(controller = owner)
                ContributeViewStates.ShowLocationError ->
                    view.locationError(controller = owner)
                ContributeViewStates.ShowItemAdded ->
                    view.showItem(added = true, controller = owner)
                ContributeViewStates.ShowItemNotAdded ->
                    view.showItem(added = false, controller = owner)
                is ContributeViewStates.ShowExtractedPdf -> {
                    Log.d(TAG, "renderViewState: ")
                    view.binding.webViewContribute.visibility = View.VISIBLE
                    view.binding.buttonPrint.visibility = View.VISIBLE
                }
                is ContributeViewStates.ShowShareDialog -> {
                    Log.d(TAG, "renderViewState: ")
                    val pdfCreator = PdfManager()
                    pdfCreator.createWebPrintJob(view.context, view.binding.webViewContribute)
                    event.onNext(ContributeEvents.ClosePdf)
                }
                ContributeViewStates.ClosePdf -> {
                    Log.d(TAG, "renderViewState: ")
                    view.binding.webViewContribute.visibility = View.GONE
                    view.binding.buttonPrint.visibility = View.GONE
                }
                else ->
                    Log.d(ContentValues.TAG, "nothing")
            }
        }
    }
}