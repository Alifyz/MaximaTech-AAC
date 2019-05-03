package com.alifyz.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alifyz.newsapp.R
import com.alifyz.newsapp.data.entity.Article
import com.bumptech.glide.Glide

class NewsAdapter : PagedListAdapter<Article, NewsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = getItem(position)
        holder.title.text = currentNews?.title
        holder.description.text = currentNews?.description

        Glide.with(holder.imageView.context)
            .load(currentNews?.urlToImage)
            .error(R.drawable.nopic_homebox)
            .into(holder.imageView)
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView = itemView.findViewById<ImageView>(R.id.img_news)
        val title = itemView.findViewById<TextView>(R.id.txt_title)
        val description = itemView.findViewById<TextView>(R.id.txt_description)
        val group = itemView.findViewById<RelativeLayout>(R.id.viewgroup)
            .setOnClickListener(this)

        override fun onClick(v: View?) {
            val selectedNews = getItem(adapterPosition)
            val bundle = bundleOf(
                "id" to selectedNews?.id)
            v?.findNavController()?.navigate(R.id.action_mainFragment_to_detailsFragment, bundle)
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.content == newItem.content

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }
}