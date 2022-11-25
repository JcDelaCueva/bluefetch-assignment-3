package com.jcdelacueva.bluefetchassignment3.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcdelacueva.bluefetchassignment3.data.model.LoginInfo
import com.jcdelacueva.bluefetchassignment3.data.repo.LoginRepository
import com.jcdelacueva.bluefetchassignment3.view.feed.FeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo: LoginRepository) : ViewModel() {

    private val mutableState: MutableSharedFlow<LoginState> = MutableSharedFlow()
    val state = mutableState.asSharedFlow()

    fun login(loginInfo: LoginInfo) {
        viewModelScope.launch {
            mutableState.emit(LoginState.DisableEditing)
            mutableState.emit(LoginState.ShowProgress)
            try {
                repo.login(loginInfo)
                mutableState.emit(LoginState.LoginSuccess)
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.emit(LoginState.EnableEditing)
                mutableState.emit((LoginState.ShowError(e)))
            }
        }
    }
}
