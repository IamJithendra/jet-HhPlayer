package com.hh.composeplayer

import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.util.PagerState

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/24  15:25
 */
class MainViewModel : BaseViewModel() {

    /**
     * Pager Number
     */
    val pagerState = PagerState(maxPage = 2)

    /**
     * Show Composable
     */
    var modelStatus : Model? by mutableStateOf( null )


    fun startModel(model: Model){
        modelStatus = model
    }

    fun endModel(){
        modelStatus = null
    }
}