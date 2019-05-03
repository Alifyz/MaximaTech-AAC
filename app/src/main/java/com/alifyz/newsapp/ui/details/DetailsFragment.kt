package com.alifyz.newsapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.alifyz.newsapp.R
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    lateinit var viewModel: DetailsViewModel
    lateinit var binding : FragmentDetailsBinding
    lateinit var article : Article

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        binding.news = article

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString("id")



       /* val args = arguments
        news_content.text = args?.getString("content")
        news_title.text = args?.getString("title")
        val link = args?.getString("link")

        Glide.with(this)
            .load(args?.getString("image"))
            .error(R.drawable.nopic_homebox)
            .into(news_image)

        fab.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(link))
            startActivity(intent)
        }

        bar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    findNavController().navigate(R.id.action_detailsFragment_to_settingsFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }*/
    }

}

