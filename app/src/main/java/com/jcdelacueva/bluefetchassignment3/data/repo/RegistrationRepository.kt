package com.jcdelacueva.bluefetchassignment3.data.repo

import com.jcdelacueva.bluefetchassignment3.data.ApiInterface
import com.jcdelacueva.bluefetchassignment3.data.SharedPrefToken
import com.jcdelacueva.bluefetchassignment3.data.Token
import com.jcdelacueva.bluefetchassignment3.data.model.LoginInfo
import com.jcdelacueva.bluefetchassignment3.data.model.RegistrationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RegistrationRepository {
    suspend fun register(registrationInfo: RegistrationInfo)
}

class RegistrationRepositoryImpl @Inject constructor(private val apiInterface: ApiInterface, private val token: Token) :
    RegistrationRepository {
    override suspend fun register(registrationInfo: RegistrationInfo) {
        return withContext(Dispatchers.IO) {
            val tokenRes = apiInterface.register(registrationInfo)
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
