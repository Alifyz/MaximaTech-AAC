package com.alifyz.newsapp.paging

import androidx.paging.PagedList
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.repository.AppRepository

class PagingCallback(val repository : AppRepository) : PagedList.BoundaryCallback<Article>() {

    var pageData = repository.getPaginationData()
    var pageToken = pageData?.lastPage ?: 1

    override fun onZeroItemsLoaded() {
       repository.fetchHeadlinesFromNetwork(pageToken)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Article) {
        refreshPageToken()
        val nextPageToken = pageToken.plus(1)
        repository.fetchHeadlinesFromNetwork(nextPageToken)
    }

    override fun onItemAtFrontLoaded(itemAtFront: Article) {
        repository.updateCachedNews()
    }

    private fun refreshPageToken() {
        pageToken = repository.getPaginationData()?.lastPage ?: 1
    }
}