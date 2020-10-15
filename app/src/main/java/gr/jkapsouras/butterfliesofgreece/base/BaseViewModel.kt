package gr.jkapsouras.butterfliesofgreece.base

import gr.jkapsouras.butterfliesofgreece.main.ViewStates.MenuViewStates

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.launch
//
//abstract class BaseViewModel : ViewModel() {
//    private val events = Channel<UiEvent>()
//    protected abstract val state: UiState
//
//    fun state() = events
//        .receiveAsFlow()
//        .domain()
//        .flatMapMerge { reduce(it) }
//        .asLiveData(viewModelScope.coroutineContext)
//
//    fun dispatch(event: UiEvent) = viewModelScope.launch { events.send(event) }
//
//    protected abstract fun Flow<UiEvent>.domain(): Flow<UiEvent>
//    protected abstract fun reduce(event: UiEvent): Flow<UiState>
//
//    override fun onCleared() {
//        events.close()
//        super.onCleared()
//    }
//
//    fun incompatibleEvent(event: UiEvent) {
//        throw IllegalStateException("ViewModel [$this] cannot handle event [$event]")
//    }
//}


abstract class UiEvent

abstract class ViewState(val isTransition: Boolean){}

sealed class GeneralViewState(isTransition:Boolean) : ViewState(isTransition) {
    object Idle : GeneralViewState(true)
}