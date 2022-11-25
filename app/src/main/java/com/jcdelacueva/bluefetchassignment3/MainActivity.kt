package com.jcdelacueva.bluefetchassignment3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jcdelacueva.bluefetchassignment3.data.Token
import com.jcdelacueva.bluefetchassignment3.view.feed.FeedActivity
import com.jcdelacueva.bluefetchassignment3.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var token: Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val currentToken = token.currentToken()
        if (currentToken.isBlank()) {
            LoginActivity.start(this)
        } else {
            FeedActivity.start(this)
        }
    }
}