package com.alifyz.newsapp.paging

import androidx.paging.PagedList
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.data.entity.Data
import com.alifyz.newsapp.repository.AppRepository

class PagingCallback(val repository: AppRepository) : PagedList.BoundaryCallback<Article>() {

    var pageData = repository.getPaginationData()
    var pageToken = pageData?.lastPage ?: 1

    override fun onZeroItemsLoaded() {
        refreshPageToken()
        repository.fetchHeadlinesFromNetwork(pageToken)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Article) {
        pageToken++
        savePageToken(pageToken)
        repository.fetchHeadlinesFromNetwork(pageToken)
    }

    override fun onItemAtFrontLoaded(itemAtFront: Article) {
        refreshPageToken()
    }

    private fun refreshPageToken() {
        pageToken = repository.getPaginationData()?.lastPage ?: 1
    }

    private fun savePageToken(currentPageToken : Int) {
        val data = Data(1, currentPageToken)
        repository.savePaginationData(data)
    }
}