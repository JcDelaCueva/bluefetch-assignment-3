package com.jcdelacueva.bluefetchassignment3.view.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcdelacueva.bluefetchassignment3.data.model.LoginInfo
import com.jcdelacueva.bluefetchassignment3.data.model.RegistrationInfo
import com.jcdelacueva.bluefetchassignment3.data.repo.LoginRepository
import com.jcdelacueva.bluefetchassignment3.data.repo.RegistrationRepository
import com.jcdelacueva.bluefetchassignment3.view.feed.FeedState
import com.jcdelacueva.bluefetchassignment3.view.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repo: RegistrationRepository) : ViewModel() {

    private val mutableState: MutableSharedFlow<RegistrationState> = MutableSharedFlow()
    val state = mutableState.asSharedFlow()

    fun register(loginInfo: RegistrationInfo) {
        viewModelScope.launch {
            mutableState.emit(RegistrationState.DisableEditing)
            mutableState.emit(RegistrationState.ShowProgress)
            try {
                repo.register(loginInfo)
                mutableState.emit(RegistrationState.RegistrationSuccess)
            } catch (e: Exception) {
                e.printStackTrace()
                mutableState.emit(RegistrationState.EnableEditing)
                mutableState.emit((RegistrationState.ShowError(e)))
            }
        }
    }
}
