package gr.jkapsouras.butterfliesofgreece.main.ViewStates

import gr.jkapsouras.butterfliesofgreece.base.UiEvent
import gr.jkapsouras.butterfliesofgreece.base.ViewState

sealed class MenuViewStates : ViewState() {
    object ToField : MenuViewStates() {
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToIntroduction : MenuViewStates(){
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToEndangered : MenuViewStates(){
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToLegal : MenuViewStates(){
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToAbout : MenuViewStates(){
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToContribute : MenuViewStates()
    {
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToOnlineRecognition : MenuViewStates(){
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToOfflineRecognition : MenuViewStates(){
        override fun isTransition(): Boolean {
            return true
        }
    }

    object ToRecognition : MenuViewStates(){
        override fun isTransition(): Boolean {
            return true
        }
    }

}

/*
enum MenuViewState:ViewState {
	case toField
	case toIntroduction
	case toEndangered
	case toLegal
	case toAbout
	case toContribute
	case toOnlineRecognition
	case toOfflineRecognition
	case toRecognition

	var isTransition:Bool{
		switch self {
			case .toField,
				 .toIntroduction,
				 .toEndangered,
				 .toLegal,
				 .toAbout,
				 .toContribute,
				 .toOnlineRecognition,
				 .toOfflineRecognition,
				 .toRecognition:
				return true
		}
	}
}
 */