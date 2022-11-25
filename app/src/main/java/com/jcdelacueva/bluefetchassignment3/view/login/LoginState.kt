package com.jcdelacueva.bluefetchassignment3.view.login

sealed class LoginState {
    object EnableEditing: LoginState()
    object DisableEditing: LoginState()
    object ShowProgress: LoginState()
    object LoginSuccess : LoginState()
    data class ShowError(val error: Exception): LoginState()
}
