package com.alifyz.newsapp.paging

import androidx.paging.PagedList
import com.alifyz.newsapp.data.entity.Article
import com.alifyz.newsapp.repository.AppRepository

class PagingCallback(val repository: AppRepository) : PagedList.BoundaryCallback<Article>()