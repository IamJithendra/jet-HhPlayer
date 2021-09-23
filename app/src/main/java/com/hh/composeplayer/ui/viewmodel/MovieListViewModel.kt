package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.util.boxProgress
import kotlinx.coroutines.flow.*

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/7  11:13
 */
class MovieListViewModel : BaseViewModel() {
    // 下拉刷新状态
    var isRefreshing by mutableStateOf(false)

    private val pageSize = 20

    fun getMovieList(state : Int,id:Long) : Flow<PagingData<Video>> {
        return  if(state == 0){
            repository.getMoviePagingData(id,pageSize).cachedIn(viewModelScope)
        } else{
            repository.getMoviePagingData(id,pageSize).cachedIn(viewModelScope)
        }
    }

    fun movieListRefresh(state : Int,id:Long){
        launch({
            isRefreshing = true
            boxProgress = true
            getMovieList(state,id)
        },{
            isRefreshing = false
            boxProgress = false
        },{
            isRefreshing = false
            boxProgress = false
            showToast(it.toString())
        })
    }
}