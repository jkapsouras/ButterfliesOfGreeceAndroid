package gr.jkapsouras.butterfliesofgreece.fragments.contribute.components

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.github.barteksc.pdfviewer.PDFView
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
import kotlinx.android.synthetic.main.view_contribute.view.*
import java.io.File

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
                    view.webView_contribute.visibility = View.VISIBLE
                    view.button_print.visibility = View.VISIBLE
                }
                is ContributeViewStates.ShowShareDialog -> {
                    Log.d(TAG, "renderViewState: ")
                    val pdfCreator = PdfManager()
                    pdfCreator.createWebPrintJob(view.context, view.webView_contribute)
                    event.onNext(ContributeEvents.ClosePdf)
                }
                ContributeViewStates.ClosePdf -> {
                    Log.d(TAG, "renderViewState: ")
                    view.webView_contribute.visibility = View.GONE
                    view.button_print.visibility = View.GONE
                }
            }
        }
    }
}