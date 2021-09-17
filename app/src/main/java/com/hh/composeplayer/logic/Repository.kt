package com.hh.composeplayer.logic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.ui.paging.HomeMovieListSource
import com.hh.composeplayer.ui.paging.SearchResultSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.logic
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/16  15:30
 */
class Repository ( private val dataHelper: HttpDataHelper ){
    /**
     * get TabList
     */
    suspend fun getTabList() = withContext(IO) {
        dataHelper.getTabList(this)
    }
//    /**
//     * get MovieList
//     */
//    suspend fun getMovieList(state :Long = 0L, pageSize : Int) = withContext(Dispatchers.IO) {
//        dataHelper.getPlayerList(state,page,pageSize)
//    }

    suspend fun getMovieDetail(ids : String) = withContext(IO) {
        dataHelper.getMovieDetail(ids,this)
    }

    fun getSearchResultList(searchName : String) : Flow<PagingData<Video>>{
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { SearchResultSource(dataHelper,searchName)}
        ).flow
    }

    /**
     * get MovieList
     */
    fun getMoviePagingData(state :Long = 0L, pageSize : Int): Flow<PagingData<Video>> {
        return Pager(
            config = PagingConfig(pageSize),
            pagingSourceFactory = { HomeMovieListSource(dataHelper,state) }
        ).flow
    }


}