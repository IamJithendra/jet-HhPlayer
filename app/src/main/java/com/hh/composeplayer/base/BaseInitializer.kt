package com.hh.composeplayer.base

import android.content.Context
import androidx.multidex.MultiDex
import androidx.startup.Initializer
import com.hjq.permissions.XXPermissions
import org.litepal.LitePal

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.base
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/30  9:50
 * 首先调用Application的attachBaseContext()方法，然后调用ContentProvider的onCreate()方法，接下来调用Application的onCreate()方法。
 * apk生成过程中class文件转换成dex文件，默认只会生成一个dex文件，单个dex文件中的方法数不能超过65536，不然编译会报错，但是我们在开发App时肯定会集成一堆库，方法数一般都是超过65536的，解决这个问题的办法就是：一个dex装不下，用多个dex来装，gradle增加一行配置：multiDexEnabled true
 * MultiDex方法数过多采用分包形式
 * litepal数据库框架
 */
class BaseInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        MultiDex.install(context)
        LitePal.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}