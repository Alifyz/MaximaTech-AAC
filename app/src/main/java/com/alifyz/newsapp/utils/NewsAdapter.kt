package com.alifyz.newsapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alifyz.newsapp.R
import com.alifyz.newsapp.data.entity.Article

class NewsAdapter(val articles : List<Article>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return articles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentArticle = articles.get(position)

        holder.title.text = currentArticle.title
        holder.description.text = currentArticle.description
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.img_news)
        val title = itemView.findViewById<TextView>(R.id.txt_title)
        val description = itemView.findViewById<TextView>(R.id.txt_description)
    }
}