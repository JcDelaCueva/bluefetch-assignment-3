package com.jcdelacueva.bluefetchassignment3.view.addfeed

sealed class AddFeedState {
    object EnableEditing: AddFeedState()
    object DisableEditing: AddFeedState()
    object ShowProgress: AddFeedState()
    object DonePosting: AddFeedState()
    data class ShowError(val error: Exception): AddFeedState()
}