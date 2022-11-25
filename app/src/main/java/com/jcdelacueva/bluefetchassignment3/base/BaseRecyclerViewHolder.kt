package com.jcdelacueva.bluefetchassignment3.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<T>(
    private val containerView: View
) : RecyclerView.ViewHolder(
    containerView
) {

    abstract fun bind(item: T)

    abstract fun prepareForReuse()
}

