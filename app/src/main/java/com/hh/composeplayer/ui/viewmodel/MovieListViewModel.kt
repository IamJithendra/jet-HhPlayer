package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.logic.HttpDataHelper
import com.hh.composeplayer.logic.Repository

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/7  11:13
 */
class MovieListViewModel() : BaseViewModel() {
    val movieList = MutableLiveData<MutableList<Video>>(ArrayList())

    var isShowError by mutableStateOf(false)

    // 下拉刷新状态
    var isRefreshing by mutableStateOf(false)

    private val repository = Repository(HttpDataHelper())

    suspend fun getMovieList(state : Int,id:Long) : List<Video>{
        return  if(state == 0){
            repository.getMovieList()
        } else{
            repository.getMovieList(id)
        }
    }

    fun movieListRefresh(state : Int,id:Long){
        launch({
            isRefreshing = true
            getMovieList(state,id)
        },{
            isRefreshing = false
        },{
            isRefreshing = false
            showToast(it.toString())
        })
    }
}