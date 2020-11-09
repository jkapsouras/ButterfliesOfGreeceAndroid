package gr.jkapsouras.butterfliesofgreece.fragments.families.printToPdf.components

import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.families.printToPdf.viewStates.PrintToPdfViewStates
import gr.jkapsouras.butterfliesofgreece.views.headerPrintToPdfView.HeaderPrintToPdfView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class PhotosToPrintHeaderComponent(private val headerView: HeaderPrintToPdfView) : UiComponent {
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(headerView.uiEvents, event)
    }

    override fun renderViewState(viewState: ViewState) {
        if (viewState is PrintToPdfViewStates) {
            when (viewState) {
                is PrintToPdfViewStates.ShowNumberOfPhotos ->
                    headerView.showPhotosToPrint(numberOfPhotos = viewState.numberOfPhotos)
                is PrintToPdfViewStates.ShowPickArrangeView -> {
//                pikcerContainerView.alpha = 1
//                model.selectedArrange = currentArrange
//                pickerView.dataSource = model
//                pickerView.delegate = model
//                pickerView.selectRow(
//                    model.findSelectedRow(currentArrange: currentArrange),
//                    inComponent: 0, animated: true)
                }
                is PrintToPdfViewStates.ArrangeViewChanged -> {
                    headerView.showArrange(arrange = viewState.currentArrange)
//                    pikcerContainerView.alpha = 0;
                }
            }
        }
    }
}