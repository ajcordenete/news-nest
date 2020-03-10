package com.aljon.newsnest.utils

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aljon.newsnest.R
import com.aljon.newsnest.model.Article
import com.aljon.newsnest.networking.ApiStatus
import com.aljon.newsnest.ui.news.NewsAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(url)
        .apply(RequestOptions().placeholder(R.drawable.ic_loading_animation)
            .error(R.drawable.ic_no_connection)
            .optionalCenterCrop()
            .fallback(android.R.color.darker_gray))
        .into(imageView)
}

@BindingAdapter("requestStatus")
fun bindRequestStatus(view: View, status: ApiStatus?) {
    view.visibility = if(status == ApiStatus.FAILED) View.VISIBLE else View.GONE
}

@BindingAdapter("submit")
fun bindSubmitList(recyclerView: RecyclerView, newsList: List<Article>?) {
    newsList?.let {
        var adapter = recyclerView.adapter as NewsAdapter
        adapter.submitList(newsList)

        Log.i("BindingAdapter", "size: ${newsList.size}")
    }
}

@BindingAdapter("date")
fun bindDatePublished(textView: TextView, timeStamp: String?) {
    timeStamp?.let {
        textView.text = timeAgo(timeStamp)
    }
}