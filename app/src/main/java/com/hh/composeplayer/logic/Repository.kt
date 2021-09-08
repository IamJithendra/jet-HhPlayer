package com.hh.composeplayer.logic

import kotlinx.coroutines.Dispatchers
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
    suspend fun getTabList() = withContext(Dispatchers.IO) {
        dataHelper.getTabList(this)
    }
    /**
     * get MovieList
     */
    suspend fun getMovieList(state :Long = 0L) = withContext(Dispatchers.IO) {
        dataHelper.getPlayerList(state,this)
    }


}