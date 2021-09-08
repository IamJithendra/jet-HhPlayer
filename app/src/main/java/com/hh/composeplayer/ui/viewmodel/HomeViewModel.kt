package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.Dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.bean.*
import com.hh.composeplayer.logic.HttpDataHelper
import com.hh.composeplayer.logic.Repository
import com.hh.composeplayer.util.boxProgress

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/31  13:11
 */
class HomeViewModel : BaseViewModel() {
    val mainTopTabTextWidthList = mutableStateListOf<Dp>() //标题长度

    val movieTabList = mutableStateListOf<Ty>()

    /**
     * Pager Number
     */
    @OptIn(ExperimentalPagerApi::class)
    val pagerState = PagerState(pageCount = 2,currentPage = 0,offscreenLimit = 2)

    private val repository = Repository(HttpDataHelper())

    fun getMovieTabList(){
        launch({
            boxProgress = true
            // 刷新任务执行
            movieTabList.addAll(repository.getTabList())
        },{
            boxProgress = false
        },{
            boxProgress = false
        }
        )

    }

}