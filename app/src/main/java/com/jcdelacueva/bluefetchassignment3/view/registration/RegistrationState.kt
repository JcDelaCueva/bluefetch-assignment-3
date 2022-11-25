package com.jcdelacueva.bluefetchassignment3.view.registration

sealed class RegistrationState {
    object EnableEditing: RegistrationState()
    object DisableEditing: RegistrationState()
    object ShowProgress: RegistrationState()
    object RegistrationSuccess : RegistrationState()
    data class ShowError(val error: Exception): RegistrationState()
}
