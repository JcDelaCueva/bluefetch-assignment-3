package com.jcdelacueva.bluefetchassignment3.data

import com.jcdelacueva.bluefetchassignment3.data.model.*
import retrofit2.http.*

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("/account/create")
    suspend fun register(@Body registrationInfo: RegistrationInfo): TokenResponse

    @Headers("Content-Type: application/json")
    @POST("/account/login")
    suspend fun login(@Body loginData: LoginInfo): TokenResponse

    @Headers("Content-Type: application/json")
    @POST("/feed/{postId}/comment")
    suspend fun postComment(@Header("Authorization") token: String, @Path("postId") postId: String, @Body body: PostTextBody): Comment

    @GET("/feed")
    suspend fun getFeed(@Header("Authorization") token: String, @Query("limit") limit: Int): List<Feed>

    @POST("/feed/post")
    suspend fun postFeed(@Header("Authorization") token: String, @Body body: PostTextBody): Feed
}