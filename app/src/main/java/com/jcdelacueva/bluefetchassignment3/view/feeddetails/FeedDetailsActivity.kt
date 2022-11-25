package com.jcdelacueva.bluefetchassignment3.view.feeddetails

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.databinding.ActivityFeedDetailsBinding
import com.jcdelacueva.bluefetchassignment3.view.feed.FeedState
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.text.SimpleDateFormat

@AndroidEntryPoint
class FeedDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedDetailsBinding
    private lateinit var adapter: CommentsAdapter

    private val viewModel: FeedDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpObserver()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(FEED, Feed::class.java)
        } else {
            intent.getParcelableExtra(FEED) as? Feed
        }?.let {
            viewModel.setFeed(it)
        }
    }

    private fun setUpView() {
        adapter = CommentsAdapter()

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@FeedDetailsActivity)
            adapter = this@FeedDetailsActivity.adapter
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnPostComment.setOnClickListener {
            val commentString = binding.etComment.text.toString()
            if (commentString.isNotBlank()) {
                binding.etComment.setText("")
                viewModel.postComment(commentString)
            }
        }
    }

    private fun setUpObserver() {
        viewModel.feed.observe(this) { feed ->
            showFeedDetails(feed)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                setProgress(state)

                when (state) {
                    is FeedDetailsState.ShowError -> {
                        showError(state.error)
                    }
                    is FeedDetailsState.EnableEditing -> {
                        enableEditing()
                    }
                    is FeedDetailsState.DisableEditing -> {
                        disableEditing()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showFeedDetails(feed: Feed) {
        with (feed) {
            binding.apply {
                tvText.text = text

                updatedAt?.let { updatedAt ->
                    var simpleDateFormat =
                        SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
                    val date = simpleDateFormat.parse(updatedAt)
                    simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
                    val formattedDate = simpleDateFormat.format(date)
                    tvTime.text = formattedDate
                }

                user?.let { user ->
                    tvName.text = "${user.firstName} ${user.lastName}"
                    val imgURL = user.profilePic
                    if (imgURL != null && imgURL.isNotBlank()) {
                        Glide.with(this@FeedDetailsActivity).load(imgURL).into(imgUser)
                    }
                }
            }
        }

        adapter.submitList(feed.commentsC)
    }

    private fun setProgress(state: FeedDetailsState) {
        binding.apply {
            cardProgress.visibility = if (state == FeedDetailsState.ShowProgress) View.VISIBLE else View.GONE
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

    private fun disableEditing() {
        binding.apply {
            btnPostComment.isEnabled = false
            etComment.isEnabled = false
        }
    }

    private fun enableEditing() {
        binding.apply {
            btnPostComment.isEnabled = true
            etComment.isEnabled = true
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        private const val FEED = "FEED"

        fun start(context: Context, feed: Feed) {
            context.startActivity(Intent(context, FeedDetailsActivity::class.java).apply {
                putExtra(FEED, feed)
            })
        }
    }
}