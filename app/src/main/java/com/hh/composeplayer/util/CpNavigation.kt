package com.hh.composeplayer.util

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.hh.composeplayer.bean.Model

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/27  10:06
 */
object CpNavigation {
    var currentScreen : Model? by mutableStateOf(Model.Main)
    val navList = mutableStateListOf(currentScreen)
    @SuppressLint("StaticFieldLeak")
    lateinit var navHostController: NavHostController
    /**
     * 跳转到某个页面
     */
    fun to(screenName: Model) {
        currentScreen = screenName
        navHostController.navigate(screenName.name)
        navList.add(screenName)
    }

    /**
     * 跳转到某个页面带参数
     */
    fun to(screenName: Model,bundle : Any) {
        currentScreen = screenName
        navHostController.navigate("${screenName.name}/$bundle")
        navList.add(screenName)
    }
//    /**
//     * 返回到上一页
//     */
    fun backAndReturnsIsLastPage(): Boolean {
        return if (navList.size == 1) {
            //当前是最后一页了，返回true
            true
        } else {
            navList.removeLast()
            currentScreen = navList.last()
            false
        }
    }
}
