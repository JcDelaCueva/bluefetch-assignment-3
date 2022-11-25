package com.jcdelacueva.bluefetchassignment3.data.model

import com.google.gson.annotations.SerializedName

data class LoginInfo(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)