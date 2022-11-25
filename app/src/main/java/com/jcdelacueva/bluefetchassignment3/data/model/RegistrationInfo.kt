package com.jcdelacueva.bluefetchassignment3.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationInfo(
    @SerializedName("firstname") val firstName: String,
    @SerializedName("lastname") val lastName: String,
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String
)