package com.jcdelacueva.bluefetchassignment3.data.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token") val token: String?,
    @SerializedName("code") val code: String?,
    @SerializedName("message") val message: String?
)