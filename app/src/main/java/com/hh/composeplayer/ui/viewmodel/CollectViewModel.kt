package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.bean.CollectBusK

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/17  10:51
 */
class CollectViewModel : BaseViewModel() {
    val collectData = mutableStateListOf<CollectBusK>()



}