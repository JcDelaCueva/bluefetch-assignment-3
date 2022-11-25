package com.jcdelacueva.bluefetchassignment3.data.repo

import com.jcdelacueva.bluefetchassignment3.data.ApiInterface
import com.jcdelacueva.bluefetchassignment3.data.SharedPrefToken
import com.jcdelacueva.bluefetchassignment3.data.Token
import com.jcdelacueva.bluefetchassignment3.data.model.LoginInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface LoginRepository {
    suspend fun login(loginInfo: LoginInfo)
}

class LoginRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface, private val token: Token) :
    LoginRepository {
    override suspend fun login(loginInfo: LoginInfo) {
        return withContext(Dispatchers.IO) {
            val tokenRes = apiInterface.login(loginInfo)
            if (tokenRes.code != null) {
                val message = tokenRes.message ?: ""
                throw IllegalStateException("${tokenRes.code}: $message")
            }

            tokenRes.token?.let {
                token.setCurrentToken(it)
            } ?: throw IllegalStateException("Token Not Found")
        }
    }
}
