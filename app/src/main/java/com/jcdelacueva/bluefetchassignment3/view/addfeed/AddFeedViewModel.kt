package com.jcdelacueva.bluefetchassignment3.view.addfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcdelacueva.bluefetchassignment3.data.model.PostTextBody
import com.jcdelacueva.bluefetchassignment3.data.repo.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFeedViewModel @Inject constructor(private val repo: FeedRepository): ViewModel() {
    private val mutableState: MutableSharedFlow<AddFeedState> = MutableSharedFlow()
    val state = mutableState.asSharedFlow()

    var isPosting = false

    fun addNewPost(text: String) {
        viewModelScope.launch {
            isPosting = true
            mutableState.emit(AddFeedState.ShowProgress)
            mutableState.emit(AddFeedState.DisableEditing)
            try {
                repo.postComment(PostTextBody(text))
                mutableState.emit(AddFeedState.DonePosting)
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.emit(AddFeedState.EnableEditing)
                mutableState.emit((AddFeedState.ShowError(e)))
            }

            isPosting = false
        }
    }
}