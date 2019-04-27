package com.alifyz.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alifyz.newsapp.R
import com.alifyz.newsapp.data.entity.Article

class NewsAdapter : PagedListAdapter<Article, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = getItem(position)
        holder.title.text = currentNews?.title
        holder.description.text = currentNews?.description
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.img_news)
        val title = itemView.findViewById<TextView>(R.id.txt_title)
        val description = itemView.findViewById<TextView>(R.id.txt_description)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                TODO("not implemented")
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                TODO("not implemented")
            }
        }
    }
}