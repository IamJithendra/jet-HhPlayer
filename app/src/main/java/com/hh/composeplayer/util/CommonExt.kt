package com.hh.composeplayer.util

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