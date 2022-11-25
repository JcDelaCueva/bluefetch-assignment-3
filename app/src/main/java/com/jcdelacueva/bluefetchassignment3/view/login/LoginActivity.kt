package com.jcdelacueva.bluefetchassignment3.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.jcdelacueva.bluefetchassignment3.data.model.LoginInfo
import com.jcdelacueva.bluefetchassignment3.databinding.ActivityLoginBinding
import com.jcdelacueva.bluefetchassignment3.view.feed.FeedActivity
import com.jcdelacueva.bluefetchassignment3.view.registration.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        addListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->

                setProgress(state)

                when (state) {
                    is LoginState.DisableEditing -> {
                        disableEditing()
                    }
                    is LoginState.EnableEditing -> {
                        enableEditing()
                    }
                    is LoginState.ShowError -> {
                        showError(state.error)
                    }
                    is LoginState.LoginSuccess -> {
                        goToFeedActivity()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun addListeners() {
        binding.btnLogIn.setOnClickListener {
            val userName = binding.tiUserName.editText?.text.toString().trim()
            val password = binding.tiPassword.editText?.text.toString().trim()

            hideKeyboard()

            viewModel.login(LoginInfo(userName, password))
        }

        binding.btnRegister.setOnClickListener {
            RegisterActivity.start(this@LoginActivity)
        }
    }

    private fun hideKeyboard() {
        currentFocus?.let {
            val imm: InputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun disableEditing() {
        binding.apply {
            tiUserName.isEnabled = false
            tiPassword.isEnabled = false
            btnLogIn.isEnabled = false
            btnRegister.isEnabled = false
        }
    }

    private fun enableEditing() {
        binding.apply {
            tiUserName.isEnabled = true
            tiPassword.isEnabled = true
            btnLogIn.isEnabled = true
            btnRegister.isEnabled = true
        }
    }

    private fun setProgress(state: LoginState) {
        binding.apply {
            cardProgress.visibility = if (state == LoginState.ShowProgress) View.VISIBLE else View.GONE
        }
    }

    private fun showError(exception: Exception) {
        if (exception is HttpException) {
            when (exception.code()) {
                401 -> showMessage("Provide username and password")
                403 -> showMessage("Invalid username and password")
            }
        } else {
            showMessage(exception.message ?: "")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun goToFeedActivity() {
        FeedActivity.start(this@LoginActivity)
        finishAffinity()
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}