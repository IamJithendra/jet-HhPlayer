package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hh.composeplayer.api.ApiService
import com.hh.composeplayer.api.TaskApi
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.base.requestNoCheck
import com.hh.composeplayer.bean.*
import com.hh.composeplayer.logic.HttpDataHelper
import com.hh.composeplayer.logic.Repository
import com.hh.composeplayer.util.PagerState
import com.hh.composeplayer.util.SingleLiveEvent
import com.hh.composeplayer.util.boxProgress
import com.hh.composeplayer.util.xmlToJson
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/31  13:11
 */
class HomeViewModel : BaseViewModel() {

    var mainTopTabState by mutableStateOf(0) //选择下标

    val mainTopTabTextWidthList = mutableStateListOf<Dp>() //标题长度

    val movieTabList = mutableStateListOf<Ty>()

    /**
     * Pager Number
     */
    val pagerState = PagerState(maxPage = 2,currentPage = 0)

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