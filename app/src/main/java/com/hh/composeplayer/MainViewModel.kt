package com.hh.composeplayer

import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.bean.Model

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
    @OptIn(ExperimentalPagerApi::class)
    val pagerState = PagerState(pageCount = 2)
}