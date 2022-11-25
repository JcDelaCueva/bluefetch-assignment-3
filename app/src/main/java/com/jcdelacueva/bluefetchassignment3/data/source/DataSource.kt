package com.jcdelacueva.bluefetchassignment3.data.source

import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.data.model.PostTextBody

interface DataSource {
    suspend fun getFeeds(token: String, limit: Int): List<Feed>

    suspend fun postFeed(token: String, textBody: PostTextBody): Feed
}