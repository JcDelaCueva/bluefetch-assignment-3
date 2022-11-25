package com.jcdelacueva.bluefetchassignment3.view.feeddetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcdelacueva.bluefetchassignment3.data.model.Comment
import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.data.model.PostTextBody
import com.jcdelacueva.bluefetchassignment3.data.repo.FeedDetailsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedDetailsViewModel @Inject constructor(private val repository: FeedDetailsRepositoryImpl): ViewModel() {

    private val mutableState: MutableSharedFlow<FeedDetailsState> = MutableSharedFlow()
    val state = mutableState.asSharedFlow()

    private val mutableUser = MutableLiveData<Feed>()
    val feed: LiveData<Feed> get() = mutableUser

    fun setFeed(feed: Feed) {
        mutableUser.value = feed
    }

    fun postComment(text: String) {
        feed.value?.id?.let { postId ->
            viewModelScope.launch {
                mutableState.emit(FeedDetailsState.ShowProgress)
                try {
                    mutableState.emit(FeedDetailsState.DisableEditing)
                    val comment = repository.postComment(postId, PostTextBody(text))
                    addComment(comment)
                    mutableState.emit(FeedDetailsState.EnableEditing)
                } catch (e: Exception) {
                    e.printStackTrace()
                    mutableState.emit(FeedDetailsState.EnableEditing)
                    mutableState.emit(FeedDetailsState.ShowError(e))
                }
            }
        }
    }

    fun addComment(comment: Comment) {
        val feed = mutableUser.value
        val comments = feed?.commentsC?.toMutableList() ?: mutableListOf()
        comments.add(comment)

        mutableUser.value = mutableUser.value?.apply {
            commentsC = comments
        }
    }
}

