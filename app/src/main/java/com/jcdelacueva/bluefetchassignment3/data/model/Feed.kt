package com.jcdelacueva.bluefetchassignment3.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Feed(
    @SerializedName("comments") var commentsRaw: @RawValue Any? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("text") var text: String? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("user") var user: User? = User(),
    var commentsC: List<Comment>? = null
): Parcelable

@Parcelize
data class Comment(
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("text") var text: String? = null,
    @SerializedName("timestamp") var timestamp: Long? = null,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("username") var username: String? = null
): Parcelable

@Parcelize
data class User(
    @SerializedName("username") var username: String? = null,
    @SerializedName("firstName") var firstName: String? = null,
    @SerializedName("lastName") var lastName: String? = null,
    @SerializedName("profilePic") var profilePic: String? = null
): Parcelable

data class PostTextBody(
    @SerializedName("text") val text: String
)