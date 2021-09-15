package com.hh.composeplayer.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.logic.HttpDataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.paging
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/15  11:21
 */
class SearchResultSource(private val dataHelper: HttpDataHelper,private val searchName:String):  PagingSource<Int, Video>() {
    override fun getRefreshKey(state: PagingState<Int, Video>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            withContext(Dispatchers.IO){
                val page = params.key ?: 1 // set page 1 as default
//                val pageSize = params.loadSize
                val repoResponse = dataHelper.getSearchResultList(searchName,page,this)
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (repoResponse.isNotEmpty()) page + 1 else null
                LoadResult.Page(repoResponse, prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}