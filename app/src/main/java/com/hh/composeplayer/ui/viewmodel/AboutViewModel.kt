package com.hh.composeplayer.ui.viewmodel

import com.hh.composeplayer.api.HttpUrl
import com.hh.composeplayer.base.BaseViewModel

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/17  14:18
 */
class AboutViewModel : BaseViewModel() {
    val apiUrl = HttpUrl.baseHostUrl
    val emailAddress = "kouzhifu@163.com"
    val projectAddress = "https://github.com/yellowhai/HhPlayer"
}