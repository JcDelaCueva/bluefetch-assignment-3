package com.jcdelacueva.bluefetchassignment3.view.feeddetails

import com.jcdelacueva.bluefetchassignment3.view.login.LoginState

sealed class FeedDetailsState {
    object EnableEditing: FeedDetailsState()
    object DisableEditing: FeedDetailsState()
    object ShowProgress: FeedDetailsState()
    data class ShowError(val error: Exception): FeedDetailsState()
}