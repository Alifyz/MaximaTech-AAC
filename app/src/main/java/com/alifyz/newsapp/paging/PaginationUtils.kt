package com.alifyz.newsapp.paging

import androidx.paging.PagedList

class PaginationUtils {

    companion object {

        private const val DEFAULT_PAGINATION_SIZE = 20
        private const val DEFAULT_PRETECH_DISTANCE = 5

        val pageConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(DEFAULT_PAGINATION_SIZE)
            .setInitialLoadSizeHint(DEFAULT_PAGINATION_SIZE)
            .setPrefetchDistance(DEFAULT_PRETECH_DISTANCE)
            .build()
    }
}