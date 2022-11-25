package com.jcdelacueva.bluefetchassignment3.di

import com.jcdelacueva.bluefetchassignment3.data.ApiInterface
import com.jcdelacueva.bluefetchassignment3.data.source.DataSource
import com.jcdelacueva.bluefetchassignment3.data.source.RemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteSource(remoteSource: RemoteSource): DataSource
}