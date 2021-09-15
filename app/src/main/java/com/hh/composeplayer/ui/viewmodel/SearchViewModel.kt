package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hh.composeplayer.HhCpApp.Companion.context
import com.hh.composeplayer.R
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.util.SettingUtil
import com.hh.composeplayer.util.stringResource
import com.hh.composeplayer.util.toJson
import kotlinx.coroutines.launch

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
    val historyDataState = mutableStateListOf<String>()
    /**
     * get
     */
    fun getHistoryData() {
        launch({
            SettingUtil.getSearchHistoryData()
        }, {
            historyDataState.clear()
            historyDataState.addAll(0,it)
        })
    }

    /**
     * clear
     */
    fun clearHistoryData(){
        historyDataState.clear()
        setHistoryData()
    }

    fun removeIt(name : String){
        historyDataState.remove(name)
        setHistoryData()
    }

    /**
     * set
     */
    fun search(){
        if(searchName == ""){
            showToast(context.stringResource(R.string.please_key_search))
            return
        }

        startComposeBundle(Model.SearchResult,searchName)
        historyDataState.let {
            if (it.contains(searchName)) {
                //当搜索历史中包含该数据时 删除
                it.remove(searchName)
            } else if (it.size >= 10) {
                //如果集合的size 有10个以上了，删除最后一个
                it.removeAt(it.size - 1)
            }
            //添加新数据到第一条
            it.add(0, searchName)
            it
        }
        setHistoryData()
    }

    /**
     * search
     */
    fun search(name : String){
        startComposeBundle(Model.SearchResult,name)
    }

    private fun setHistoryData(){
        viewModelScope.launch {
            SettingUtil.setSearchHistoryData(historyDataState.toJson())
        }
    }
}