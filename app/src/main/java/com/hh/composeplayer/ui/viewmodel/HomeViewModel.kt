package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
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
    // 下拉刷新状态
    var isRefreshing by mutableStateOf(false)

    var isShowError by mutableStateOf(-1)

    var mainTopTabState by mutableStateOf(0) //选择下标

    val mainTopTabTextWidthList = mutableStateListOf<Dp>() //标题长度

    val movieTabList = mutableStateListOf<Ty>()

    val movieList = SingleLiveEvent<MutableList<Video>>()

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

    fun movieListRefresh(){
        launch({
            isRefreshing = true
            getMovieList(mainTopTabState)
        },{
            isRefreshing = false
        },{
            isRefreshing = false
            showToast(it.toString())
        })
    }

    suspend fun getMovieList(state : Int) : List<Video>{
        if(state == 0){
            movieList.value?.clear()
            movieList.value?.addAll(repository.getMovieList())
        } else{
            movieList.value?.clear()
            movieList.value?.addAll(repository.getMovieList(movieTabList[mainTopTabState].staffId))
        }
        return  if(state == 0){
            repository.getMovieList()
        } else{
            repository.getMovieList(movieTabList[state].staffId)
        }
    }

//    fun getList(): MutableList<UserInfo> {
//        val info = UserInfo()
//        info.userName = "阿萨德"
//        info.address = "From Beijing, China"
//        info.pic =
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2Fc8%2Fdd%2Fb9%2Fc8ddb934a69d90216f1b406cf3975475.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631345429&t=afe5926541021684ea48777286d6575c"
//        val info1 = UserInfo()
//        info1.userName = "阿什杜"
//        info1.address = "From Beijing, China"
//        info1.pic =
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2Fc8%2Fdd%2Fb9%2Fc8ddb934a69d90216f1b406cf3975475.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631345429&t=afe5926541021684ea48777286d6575c"
//        val info2 = UserInfo()
//        info2.userName = "阿斯顿"
//        info2.address = "From Beijing, China"
//        info2.pic =
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2Fc8%2Fdd%2Fb9%2Fc8ddb934a69d90216f1b406cf3975475.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631345429&t=afe5926541021684ea48777286d6575c"
//        val info3 = UserInfo()
//        info3.userName = "爱仕达"
//        info3.address = "From Beijing, China"
//        info3.pic =
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2Fc8%2Fdd%2Fb9%2Fc8ddb934a69d90216f1b406cf3975475.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631345429&t=afe5926541021684ea48777286d6575c"
//        val info4 = UserInfo()
//        info4.userName = "阿斯达as打死代金券win参考恰恰是肯德基诶阿什杜"
//        info4.address = "From Beijing, China"
//        info4.pic =
//            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2Fc8%2Fdd%2Fb9%2Fc8ddb934a69d90216f1b406cf3975475.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1631345429&t=afe5926541021684ea48777286d6575c"
//
//
//        return mutableListOf(
//            info,
//            info1,
//            info2,
//            info4,
//        )
//    }
}