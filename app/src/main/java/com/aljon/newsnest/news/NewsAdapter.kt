package com.aljon.newsnest.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aljon.newsnest.R
import com.aljon.newsnest.model.Article
import com.aljon.newsnest.databinding.ListArticleItemBinding
import com.aljon.newsnest.databinding.ListSearchItemBinding

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class NewsAdapter(val onItemSelectListener: OnItemSelectListener): ListAdapter<Article, RecyclerView.ViewHolder>(NewsDiffUtil) {

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) ITEM_VIEW_TYPE_HEADER else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_HEADER -> ViewHolder(ListArticleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> ViewHolderSearch(ListSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var articles: Article = getItem(position)

        when(holder) {
            is ViewHolder -> holder.bind(articles)
            is ViewHolderSearch -> holder.bind(articles)
        }

        holder.itemView.setOnClickListener {
            onItemSelectListener.onClick(articles.url)
        }
    }

    class ViewHolder(private var binding: ListArticleItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.article = article
            binding.executePendingBindings()
        }
    }

    class ViewHolderSearch(private var binding: ListSearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            binding.article = article
            binding.executePendingBindings()
        }
    }

    companion object NewsDiffUtil: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title.equals(newItem.title, true)
        }
    }

    class OnItemSelectListener(val clickListener: (url: String) -> Unit ) {
        fun onClick(url: String) = clickListener(url)
    }
}