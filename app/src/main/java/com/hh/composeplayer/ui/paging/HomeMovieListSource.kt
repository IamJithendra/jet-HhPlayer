package com.hh.composeplayer.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.logic.HttpDataHelper
import com.hh.composeplayer.logic.Repository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.paging
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/9  13:15
 */
class HomeMovieListSource (private val dataHelper: HttpDataHelper, val id:Long) : PagingSource<Int, Video>() {
    override fun getRefreshKey(state: PagingState<Int, Video>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Video> {
        return try {
            withContext(IO){
                val page = params.key ?: 1 // set page 1 as default
//                val pageSize = params.loadSize
                val repoResponse = dataHelper.getPlayerList(id,page,this)
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (repoResponse.isNotEmpty()) page + 1 else null
                LoadResult.Page(repoResponse, prevKey, nextKey)
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}