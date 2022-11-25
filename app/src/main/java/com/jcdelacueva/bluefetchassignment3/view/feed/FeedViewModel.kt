package com.jcdelacueva.bluefetchassignment3.view.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcdelacueva.bluefetchassignment3.data.repo.FeedRepository
import com.jcdelacueva.bluefetchassignment3.view.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val repo: FeedRepository) : ViewModel() {

    private val mutableState: MutableSharedFlow<FeedState> = MutableSharedFlow()
    val state = mutableState.asSharedFlow()

    fun getFeeds() {
        viewModelScope.launch {
            mutableState.emit(FeedState.ShowProgress)
            try {
                val feeds = repo.getFeeds()
                mutableState.emit(FeedState.LoadFeeds(feeds))
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.emit((FeedState.ShowError(e)))
            }
        }
    }

    fun setLimit(limit: Int) {
        repo.setLimit(limit)
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
            mutableState.emit(FeedState.Logout)
        }
    }
}