package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.bean.*
import com.hh.composeplayer.util.Mylog
import com.hh.composeplayer.util.boxProgress
import org.litepal.LitePal
import org.litepal.extension.findAll

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

    var isShowError by mutableStateOf(false)

    init {
        movieTabList.addAll(LitePal.findAll<Ty>().filter{ it.staffId.toInt()!=17 && it.staffId.toInt()!=18 && it.staffId.toInt()!=24
        })
        if(movieTabList.size!=0){
            movieTabList.add(0,Ty("最新"))
        }
    }
    /**
     * Pager Number
     */
    @OptIn(ExperimentalPagerApi::class)
    val pagerState = PagerState(pageCount = 2,currentPage = 0,offscreenLimit = 1)

    fun getMovieTabList(){
        launch({
            boxProgress = true
            isShowError = false
            // 刷新任务执行
            movieTabList.addAll(repository.getTabList())
        },{
            boxProgress = false
            isShowError = false
        },{
            boxProgress = false
            isShowError = true
        }
        )

    }

}