package gr.jkapsouras.butterfliesofgreece.fragments.contribute.components

import android.util.Log
import gr.jkapsouras.butterfliesofgreece.MainActivity
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.contribute.viewStates.ContributeViewStates
import gr.jkapsouras.butterfliesofgreece.managers.LocationManager.Companion.TAG
import gr.jkapsouras.butterfliesofgreece.views.contributeView.ContributeView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class ContributeComponent(private val view: ContributeView,
private  val owner: MainActivity) : UiComponent {
    //    let owner:UIViewController
//    let navigationItem:UINavigationItem
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>
//    let pdfView:PDFView
//    var share:UIBarButtonItem?
//    var close:UIBarButtonItem?
//    var info:UIBarButtonItem

    init {
//    self.owner = owner
//    self.navigationItem = navigationItem
        uiEvents = Observable.merge(view.uiEvents, event)
        // Create the info button
//    let infoButton = UIButton(type: .infoLight)
//    infoButton.tintColor = Constants.Colors.contribute(darkMode: true).color

//    if let pdf = parentView.subviews.first(where: {v in v is PDFView}){
//        pdfView = pdf as! PDFView
//    }
//    else{
//        pdfView = PDFView(frame: parentView.bounds)
//        parentView.addSubview(pdfView)
//    }
//    pdfView.alpha = 0
//    pdfView.backgroundColor = Constants.Colors.contribute(darkMode: false).color
//    // You will need to configure the target action for the button itself, not the bar button itemr
//
//    // Create a bar button item using the info button as its custom view
//    info = UIBarButtonItem(customView: infoButton)
//    infoButton.addTarget(self, action: #selector(infoTapped), for: .touchUpInside)
//
//    // Use it as required
//    self.navigationItem.setRightBarButton(info, animated: true)
    }

//    @objc func infoTapped(){
//    emitter.onNext(ContributeEvents.instructionsClicked)
//}
//
//    @objc func shareTapped(){
//    emitter.onNext(ContributeEvents.sharePdf)
//}
//
//    @objc func closeTapped(){
//    emitter.onNext(ContributeEvents.closePdf)
//}

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
                is ContributeViewStates.ShowExtractedPdf ->
                    Log.d(TAG, "renderViewState: ")
//            pdfView.document = PDFDocument(data: data)
//            pdfView.autoScales = true
//            pdfView.alpha = 1
//            share = UIBarButtonItem(barButtonSystemItem: .action, target: self, action: #selector(shareTapped))
//            let closeButton = UIButton()
//            closeButton.setTitle("", for: .normal)
//            closeButton.imageEdgeInsets = UIEdgeInsets(top: 4, left: 4, bottom: 4, right: 4)
//            closeButton.setImage(UIImage(imageLiteralResourceName: "closeX").withRenderingMode(.alwaysTemplate), for: .normal)
//            closeButton.tintColor = Constants.Colors.contribute(darkMode: true).color
//            closeButton.addTarget(self, action: #selector(closeTapped), for: .touchUpInside)
//            close = UIBarButtonItem(customView: closeButton)
//            navigationItem.setRightBarButtonItems([share!, close!], animated: true)
                is ContributeViewStates.ShowShareDialog ->
                    Log.d(TAG, "renderViewState: ")
//            let vc = UIActivityViewController(
//                    activityItems: [pdfData],
//            applicationActivities: []
//            )
//            owner.present(vc, animated: true, completion: nil)
                ContributeViewStates.ClosePdf ->
                    Log.d(TAG, "renderViewState: ")
//            pdfView.alpha = 0
//            navigationItem.setRightBarButtonItems(nil, animated: true)
//            navigationItem.setRightBarButton(info, animated: true)
            }
        }
    }
}