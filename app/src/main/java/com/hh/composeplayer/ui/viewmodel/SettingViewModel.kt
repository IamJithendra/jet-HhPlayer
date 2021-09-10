package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hh.composeplayer.base.BaseViewModel

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/2  9:54
 */
class SettingViewModel : BaseViewModel(){
    var caCheSize by mutableStateOf("0.0MB")
}