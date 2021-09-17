package com.hh.composeplayer.util

import android.view.View
import com.alibaba.fastjson.JSON

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/14  8:42
 */
/**
 * 将对象转为JSON字符串
 */
fun Any?.toJson():String{
    return JSON.toJSON(this).toString()
}

/**
 * px值转换成dp
 */
fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}