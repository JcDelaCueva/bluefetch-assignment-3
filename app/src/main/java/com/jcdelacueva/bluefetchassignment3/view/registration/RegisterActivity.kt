package com.jcdelacueva.bluefetchassignment3.view.registration

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.jcdelacueva.bluefetchassignment3.data.model.RegistrationInfo
import com.jcdelacueva.bluefetchassignment3.databinding.ActivityRegisterBinding
import com.jcdelacueva.bluefetchassignment3.view.feed.FeedActivity
import com.jcdelacueva.bluefetchassignment3.view.login.LoginState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setUpListeners()
        setUpObservers()
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                setProgress(state)

                when (state) {
                    is RegistrationState.DisableEditing -> {
                        disableEditing()
                    }
                    is RegistrationState.EnableEditing -> {
                        enableEditing()
                    }
                    is RegistrationState.ShowError -> {
                        showError(state.error)
                    }
                    is RegistrationState.RegistrationSuccess -> {
                        goToFeedActivity()
                    }
                    else -> {}
                }

            }
        }
    }

    private fun setUpListeners() {
        binding.btnRegister.setOnClickListener {
            hideKeyboard()

            val firstName = binding.tiFirstName.editText?.text.toString()
            val lastName = binding.tiLastName.editText?.text.toString()
            val userName = binding.tiUserName.editText?.text.toString()
            val password = binding.tiPassword.editText?.text.toString()

            viewModel.register(RegistrationInfo(
                firstName,
                lastName,
                userName,
                password
            ))
        }

        binding.btnLogIn.setOnClickListener {
            finish()
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

    private fun setProgress(state: RegistrationState) {
        binding.apply {
            cardProgress.visibility = if (state == RegistrationState.ShowProgress) View.VISIBLE else View.GONE
        }
    }

    private fun showError(exception: Exception) {
        if (exception is HttpException) {
            when (exception.code()) {
                401 -> showMessage("Provide username and password")
                402 -> showMessage("Invalid username and password")
            }
        } else {
            showMessage(exception.message ?: "")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun goToFeedActivity() {
        FeedActivity.start(this@RegisterActivity)
        finishAffinity()
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }
    }
}