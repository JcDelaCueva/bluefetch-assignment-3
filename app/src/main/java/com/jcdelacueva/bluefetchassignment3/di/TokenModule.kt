package com.jcdelacueva.bluefetchassignment3.di

import com.jcdelacueva.bluefetchassignment3.data.SharedPrefToken
import com.jcdelacueva.bluefetchassignment3.data.Token
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {

    @Binds
    @Singleton
    abstract fun bindSharedPrefToken(token: SharedPrefToken): Token
}
