package com.jcdelacueva.bluefetchassignment3.view.feeddetails


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.jcdelacueva.bluefetchassignment3.base.BaseRecyclerViewHolder
import com.jcdelacueva.bluefetchassignment3.data.model.Feed
import com.jcdelacueva.bluefetchassignment3.R
import com.jcdelacueva.bluefetchassignment3.data.model.Comment
import com.jcdelacueva.bluefetchassignment3.databinding.ItemCommentBinding
import com.jcdelacueva.bluefetchassignment3.databinding.ItemFeedBinding
import java.text.SimpleDateFormat

class CommentsAdapter : ListAdapter<Comment, CommentsAdapter.ViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(ItemCommentBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    class ViewHolder(private val binding: ItemCommentBinding) :
        BaseRecyclerViewHolder<Comment>(binding.root) {

        override fun bind(item: Comment) {
            binding.apply {
                tvTime.text = item.updatedAt
                tvName.text = item.username
                tvText.text = item.text
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
        private val diffCallBack = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem == newItem
        }
    }
}
