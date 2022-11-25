package com.jcdelacueva.bluefetchassignment3.data.repo

import com.jcdelacueva.bluefetchassignment3.data.Token
import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.data.model.PostTextBody
import com.jcdelacueva.bluefetchassignment3.data.source.DataSource
import javax.inject.Inject

interface FeedRepository {

    fun setLimit(limit: Int)

    suspend fun getFeeds(): List<Feed>

    suspend fun postComment(textBody: PostTextBody): Feed

    suspend fun logout()
}

class FeedRepositoryImpl @Inject constructor(
    private val token: Token,
    private val source: DataSource
) : FeedRepository {

    private var limit = 25

    override fun setLimit(limit: Int) {
        this.limit = limit
    }

    override suspend fun getFeeds(): List<Feed> {
        return source.getFeeds(token.currentToken(), limit)
    }

    override suspend fun postComment(textBody: PostTextBody): Feed {
        return source.postFeed(token.currentToken(), textBody)
    }

    override suspend fun logout() {
        token.setCurrentToken("")
    }
}