package com.jcdelacueva.bluefetchassignment3.view.feed


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.jcdelacueva.bluefetchassignment3.base.BaseRecyclerViewHolder
import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.R
import com.jcdelacueva.bluefetchassignment3.databinding.ItemFeedBinding
import java.text.SimpleDateFormat

class FeedAdapter : ListAdapter<Feed, FeedAdapter.ViewHolder>(diffCallBack) {

    interface ItemClickListener {
        fun onItemClick(feed: Feed)
    }

    var itemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)
        return ViewHolder(ItemFeedBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feed = getItem(holder.adapterPosition)
        holder.bind(feed)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(feed)
        }
    }

    class ViewHolder(private val binding: ItemFeedBinding) :
        BaseRecyclerViewHolder<Feed>(binding.root) {

        override fun bind(item: Feed) {
            with(item) {
                binding.apply {
                    tvText.text = item.text

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
                            Glide.with(itemView).load(imgURL).into(imgUser)
                        }
                    }

                    tvComments.text = "Comments (${commentsC?.size ?: 0})"
                }
            }
        }

        override fun prepareForReuse() {
            binding.apply {
                tvName.text = ""
                tvText.text = ""
                tvTime.text = ""
            }
        }
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem == newItem
        }
    }
}
