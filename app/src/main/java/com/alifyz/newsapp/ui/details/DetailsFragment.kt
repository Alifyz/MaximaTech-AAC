package com.alifyz.newsapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.alifyz.newsapp.R
import com.alifyz.newsapp.application.loadRemote
import com.alifyz.newsapp.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    lateinit var viewModel: DetailsViewModel
    lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong("id") ?: 0

        viewModel.searchNews(id).observe(this, Observer { currentArticle ->
            binding.news = currentArticle
            currentArticle.urlToImage?.let { url ->
                news_image.loadRemote(url)
            }
        })

        fab.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(binding.news?.url))
            startActivity(intent)
        }

        bar.setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.settings -> {
                findNavController().navigate(R.id.action_detailsFragment_to_settingsFragment)
                true
            }
            else -> {
                false
            }
        }
    }
}

