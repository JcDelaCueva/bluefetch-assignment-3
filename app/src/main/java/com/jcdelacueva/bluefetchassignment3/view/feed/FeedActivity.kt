package com.jcdelacueva.bluefetchassignment3.view.feed

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.R
import com.jcdelacueva.bluefetchassignment3.databinding.ActivityFeedBinding
import com.jcdelacueva.bluefetchassignment3.view.addfeed.AddFeedActivity
import com.jcdelacueva.bluefetchassignment3.view.feeddetails.FeedDetailsActivity
import com.jcdelacueva.bluefetchassignment3.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class FeedActivity : AppCompatActivity(), FeedAdapter.ItemClickListener {

    private val viewModel: FeedViewModel by viewModels()

    private lateinit var binding: ActivityFeedBinding
    private lateinit var adapter: FeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupObservers()
        loadData()
        addListeners()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                setProgress(state)

                when (state) {
                    is FeedState.LoadFeeds -> {
                        adapter.submitList(state.feeds)
                    }
                    is FeedState.ShowError -> {
                        showError(state.error)
                    }
                    is FeedState.Logout -> {
                        logout()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setupView() {
        adapter = FeedAdapter()
        adapter.itemClickListener = this

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@FeedActivity)
            adapter = this@FeedActivity.adapter
        }
    }

    private fun setProgress(state: FeedState) {
        binding.apply {
            cardProgress.visibility = if (state == FeedState.ShowProgress) View.VISIBLE else View.GONE
            swipeRefresh.isRefreshing = state == FeedState.ShowProgress
        }
    }

    private fun showError(exception: Exception) {
        if (exception is HttpException) {
            when (exception.code()) {
                401 -> showMessage("Invalid Request")
                403 -> showMessage("Not Authorized")
            }
        } else {
            showMessage(exception.message ?: "")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun logout() {
        LoginActivity.start(this)
        finishAffinity()
    }

    private fun loadData() {
        viewModel.getFeeds()
    }

    private fun addListeners() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_logout -> {
                    showLogoutDialog()
                    true
                }

                else -> false
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFeeds()
        }

        binding.fabAddPost.setOnClickListener {
            AddFeedActivity.start(this@FeedActivity)
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationDrawer.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.isChecked) {
                return@setNavigationItemSelectedListener true
            }

            when (menuItem.itemId) {
                R.id.menu_post_5 -> {
                    menuItem.isChecked = true
                    viewModel.setLimit(LIMIT_5)
                }
                R.id.menu_post_10 -> {
                    menuItem.isChecked = true
                    viewModel.setLimit(LIMIT_10)
                }
                R.id.menu_post_15 -> {
                    menuItem.isChecked = true
                    viewModel.setLimit(LIMIT_15)
                }
                R.id.menu_post_20 -> {
                    menuItem.isChecked = true
                    viewModel.setLimit(LIMIT_20)
                }
                R.id.menu_post_25 -> {
                    menuItem.isChecked = true
                    viewModel.setLimit(LIMIT_25)
                }
            }

            viewModel.getFeeds()
            binding.drawerLayout.close()
            true
        }
    }

    override fun onItemClick(feed: Feed) {
        FeedDetailsActivity.start(this, feed)
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("You're about to logout your account.")
            .setNegativeButton("Cancel") { _, _ ->

            }
            .setPositiveButton("Logout") { _, _ ->
                viewModel.logout()
            }
            .show()
    }

    companion object {

        const val LIMIT_5 = 5
        const val LIMIT_10 = 10
        const val LIMIT_15 = 15
        const val LIMIT_20 = 20
        const val LIMIT_25 = 25

        fun start(context: Context) {
            context.startActivity(Intent(context, FeedActivity::class.java))
        }
    }
}