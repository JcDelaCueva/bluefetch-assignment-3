package com.jcdelacueva.bluefetchassignment3.di

import com.jcdelacueva.bluefetchassignment3.data.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideClient(interceptor: HttpLoggingInterceptor) = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
        writeTimeout(30, TimeUnit.SECONDS)
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)

    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder().apply {
        baseUrl("https://us-central1-bluefletch-learning-assignment.cloudfunctions.net")
        addConverterFactory(GsonConverterFactory.create())
        client(client)
    }.build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit) = retrofit.create(ApiInterface::class.java)
}