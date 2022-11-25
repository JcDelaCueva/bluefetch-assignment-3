package com.jcdelacueva.bluefetchassignment3.data.source

import com.jcdelacueva.bluefetchassignment3.data.ApiInterface
import com.jcdelacueva.bluefetchassignment3.data.model.Comment
import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.data.model.PostTextBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteSource @Inject constructor(private val apiInterface: ApiInterface) :
    DataSource {

    override suspend fun getFeeds(token: String, limit: Int): List<Feed> {
        return withContext(Dispatchers.IO) {
            val feeds = apiInterface.getFeed(token, limit)

            for (feed in feeds) {
                feed.commentsC = createCommentsList(feed.commentsRaw)
            }

            feeds.map { feed ->
                feed.apply {
                    commentsC = createCommentsList(feed.commentsRaw)
                }
            }
        }
    }

    override suspend fun postFeed(token: String, textBody: PostTextBody): Feed {
        return withContext(Dispatchers.IO) {
            apiInterface.postFeed(token, textBody)
        }
    }

    private fun createCommentsList(rawCommentData: Any?): List<Comment> {
        return when (rawCommentData) {
            is Map<*, *> -> {
                createCommentListFromMap(rawCommentData)
            }
            is List<*> -> {
                createCommentListFromMap(rawCommentData)
            }
            else -> {
                emptyList()
            }
        }
    }

    private fun createCommentListFromMap(map: Map<*, *>): List<Comment> {
        val comments = mutableListOf<Comment>()
        var item: Map<String, Any>
        for (key in map.keys) {
            item = map[key] as Map<String, Any>
            comments.add(Comment(
                createdAt = item["createdAt"] as? String,
                id = item["id"] as? Int,
                text = item["text"] as? String,
                timestamp = item["timestamp"] as? Long,
                updatedAt = item["updatedAt"] as? String,
                username = item["username"] as? String
            ))
        }

        return comments
    }

    private fun createCommentListFromMap(list: List<*>): List<Comment> {
        val comments = mutableListOf<Comment>()
        var item: Map<String, Any>
        for (c in list) {
            item = c as Map<String, Any>
            comments.add(Comment(
                createdAt = item["createdAt"] as? String,
                id = item["id"] as? Int,
                text = item["text"] as? String,
                timestamp = item["timestamp"] as? Long,
                updatedAt = item["updatedAt"] as? String,
                username = item["username"] as? String
            ))
        }

        return comments
    }
}