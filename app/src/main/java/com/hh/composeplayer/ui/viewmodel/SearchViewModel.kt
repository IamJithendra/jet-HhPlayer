package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.util.SettingUtil

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/1  15:33
 */
class SearchViewModel : BaseViewModel() {
    var searchName by mutableStateOf("")

    //搜索历史词数据
    val historyData = MutableLiveData<List<String>>(ArrayList())

    /**
     * 获取历史数据
     */
    fun getHistoryData() {
        launch({
            SettingUtil.getSearchHistoryData()
        }, {
            historyData.value = it
        }, {
            //获取本地历史数据出异常了
            showToast("获取本地历史数据出异常了")
        })
    }
}