package com.jcdelacueva.bluefetchassignment3.view.addfeed

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.jcdelacueva.bluefetchassignment3.R
import com.jcdelacueva.bluefetchassignment3.databinding.ActivityAddFeedBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class AddFeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFeedBinding
    private val viewModel: AddFeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpObservers()
        setUpListeners()
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                setProgress(state)

                when(state) {
                    is AddFeedState.DisableEditing -> {
                        disableEditing()
                    }
                    is AddFeedState.EnableEditing -> {
                        enableEditing()
                    }
                    is AddFeedState.ShowError -> {
                        showError(state.error)
                    }
                    is AddFeedState.DonePosting -> {
                        finish()
                    }
                    else -> {}
                }

            }
        }
    }

    private fun setUpListeners() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_post -> {
                    if (viewModel.isPosting) true

                    addNewPost()

                    true
                }

                else -> false
            }
        }
    }

    private fun addNewPost() {
        val postString = binding.etPost.text.toString().trim()
        if (postString.isNotBlank()) {
            viewModel.addNewPost(postString)
        }
    }

    private fun setProgress(state: AddFeedState) {
        binding.apply {
            cardProgress.visibility = if (state == AddFeedState.ShowProgress) View.VISIBLE else View.GONE
        }
    }

    private fun disableEditing() {
        binding.apply {
            etPost.isEnabled = false
        }
    }

    private fun enableEditing() {
        binding.apply {
            etPost.isEnabled = true
        }
    }

    private fun showError(exception: Exception) {
        if (exception is HttpException) {
            when (exception.code()) {
                401 -> showMessage("Invalid Request")
                403 -> showMessage("Unauthorized")
                500 -> showMessage("Internal Server Error")
            }
        } else {
            showMessage(exception.message ?: "")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, AddFeedActivity::class.java))
        }
    }
}