package com.jcdelacueva.bluefetchassignment3.data.repo

import com.jcdelacueva.bluefetchassignment3.data.ApiInterface
import com.jcdelacueva.bluefetchassignment3.data.Token
import com.jcdelacueva.bluefetchassignment3.data.model.Comment
import com.jcdelacueva.bluefetchassignment3.data.model.PostTextBody
import javax.inject.Inject

interface FeedDetailsRepository {
    suspend fun postComment(postId: String, commentBody: PostTextBody): Comment
}

class FeedDetailsRepositoryImpl @Inject constructor(
    private val token: Token,
    private val api: ApiInterface
) : FeedDetailsRepository {

    override suspend fun postComment(postId: String, commentBody: PostTextBody): Comment {
        return api.postComment(token.currentToken(), postId, commentBody)
    }
}
