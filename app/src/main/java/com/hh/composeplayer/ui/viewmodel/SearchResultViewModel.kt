package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.bean.Video
import kotlinx.coroutines.flow.Flow

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/14  14:06
 */
class SearchResultViewModel : BaseViewModel() {
    val searchName = mutableStateOf("")

    // 下拉刷新状态
    var isRefreshing by mutableStateOf(false)

    fun getSearchResult() : Flow<PagingData<Video>> {
        return repository.getSearchResultList(searchName.value).cachedIn(viewModelScope)
    }
}