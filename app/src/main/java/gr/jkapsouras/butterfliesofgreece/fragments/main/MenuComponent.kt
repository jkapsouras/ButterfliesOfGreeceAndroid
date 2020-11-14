package gr.jkapsouras.butterfliesofgreece.fragments.main

import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.main.events.MenuUiEvents
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class MenuComponent : UiComponent
{
    override var uiEvents: Observable<UiEvent>
    private  val fieldButton:ConstraintLayout
    private  val contributeButton:ConstraintLayout
    private  val aboutButton:ConstraintLayout
    private  val introductionButton:ConstraintLayout
    private  val endangeredButton:ConstraintLayout
    private  val legalButton:ConstraintLayout
    private  val recognitionButton:ConstraintLayout


    constructor(field:ConstraintLayout, contribute:ConstraintLayout, about:ConstraintLayout, introduction:ConstraintLayout, endangered:ConstraintLayout, legal:ConstraintLayout, recognition:ConstraintLayout) {
        fieldButton=field
        contributeButton=contribute
        aboutButton = about
        introductionButton = introduction
        endangeredButton = endangered
        legalButton = legal
        recognitionButton = recognition

        val emitter = PublishSubject.create<UiEvent>()

        fieldButton.setOnClickListener {
            emitter.onNext(MenuUiEvents.FieldClicked)
            Log.d(Constraints.TAG, "field clicked")
        }

        contributeButton.setOnClickListener {
            emitter.onNext(MenuUiEvents.ContributeClicked)
            Log.d(Constraints.TAG, "contribute clicked")
        }

        //        Observable.merge(FieldButton.rx.tap.map{tap in
//            MenuEvent.fieldClicked as UiEvent},
//    ContributeButton.rx.tap.map{tap in
//            MenuEvent.contributeClicked as UiEvent},
//    AboutButton.rx.tap.map{tap in
//            MenuEvent.aboutClicked as UiEvent},
//    IntroductionButton.rx.tap.map{tap in
//            MenuEvent.introductionClicked as UiEvent},
//    EndangeredButton.rx.tap.map{tap in
//            MenuEvent.endangeredSpeciesClicked as UiEvent},
//    LegalButton.rx.tap.map{tap in
//            MenuEvent.legalClicked as UiEvent},
//    RecognitionButton.rx.tap.map{tap in
//            MenuEvent.recognitionClicked
//    })

    uiEvents = emitter
}

    override fun renderViewState(viewState: ViewState) {
        Log.d(Constraints.TAG, "renderViewState: ")
    }
}