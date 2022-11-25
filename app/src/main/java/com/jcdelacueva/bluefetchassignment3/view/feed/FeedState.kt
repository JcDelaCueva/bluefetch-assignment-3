package com.jcdelacueva.bluefetchassignment3.view.feed

import com.jcdelacueva.bluefetchassignment3.data.model.Feed

sealed class FeedState {
    object ShowProgress: FeedState()
    data class LoadFeeds(val feeds: List<Feed>): FeedState()
    data class ShowError(val error: Exception): FeedState()
    object Logout: FeedState()
}