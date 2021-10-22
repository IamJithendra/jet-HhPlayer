package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.bean.Video
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/14  14:06
 */
class SearchResultViewModel : BaseViewModel() {
    val searchName = MutableLiveData("")

    // 下拉刷新状态
    var isRefreshing by mutableStateOf(false)

    fun getSearchResult() : Flow<PagingData<Video>> {
        return repository.getSearchResultList(searchName.value!!).flowOn(IO)
    }
}