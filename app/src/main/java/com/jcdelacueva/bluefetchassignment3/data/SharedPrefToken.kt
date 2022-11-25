package com.jcdelacueva.bluefetchassignment3.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

interface Token {
    fun currentToken():String
    fun setCurrentToken(token: String)
}

class SharedPrefToken @Inject constructor(context: Application): Token {
    private val SHARED_PREF = "sharedPref"
    private val TOKEN = "token"
    private val prefs: SharedPreferences

    private var currentToken: String? = null

    init {
        prefs = context.getSharedPreferences(SHARED_PREF, 0)
        currentToken = prefs.getString(TOKEN, "")
    }

    override fun currentToken(): String {
        return currentToken ?: ""
    }

    override fun setCurrentToken(token: String) {
        prefs.edit().putString(TOKEN, token).apply()
        currentToken = token
    }
}
