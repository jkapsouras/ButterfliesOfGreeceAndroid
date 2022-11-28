package gr.jkapsouras.butterfliesofgreece.fragments.families.components

import android.content.ContentValues
import android.util.Log
import gr.jkapsouras.butterfliesofgreece.base.UiComponent
import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState
import gr.jkapsouras.butterfliesofgreece.fragments.families.ViewArrange
import gr.jkapsouras.butterfliesofgreece.fragments.families.viewStates.FamiliesViewViewStates
import gr.jkapsouras.butterfliesofgreece.views.photosCollectionView.PhotosCollectionView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class FamiliesCollectionComponent(private val photosCollectionView: PhotosCollectionView) : UiComponent
{
    private val event: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    override val uiEvents: Observable<UiEvent>

    init {
        uiEvents = Observable.merge(photosCollectionView.uiEvents,event)
    }

    override  fun renderViewState(viewState: ViewState) {
        if (viewState is FamiliesViewViewStates){
            when (viewState) {
                is FamiliesViewViewStates.ShowFamilies -> {
                    print("number of families: ${viewState.families.count()}")
                    photosCollectionView.showFamilies(viewState.families)
                }
                is FamiliesViewViewStates.SwitchViewStyle -> {
                    if (viewState.currentArrange == ViewArrange.Grid) {
                        photosCollectionView.show()
                    } else {
                        photosCollectionView.hide()
                    }
                }
                else ->
                    Log.d(ContentValues.TAG, "nothing")
            }
        }
    }
}