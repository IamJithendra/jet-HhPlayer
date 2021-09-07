package com.hh.composeplayer.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hh.composeplayer.HhCpApp
import com.hh.composeplayer.R
import java.lang.reflect.InvocationTargetException
import kotlin.math.roundToInt


object SettingUtil {
    /**
     * 获取当前主题颜色
     */
    suspend fun getColor(): Int {
        return DataStorePreference.getIntValue("color",Color.parseColor("#2196F3"))
    }
//
    /**
     * 设置主题颜色
     */
    suspend fun setColor(color: Int) {
        DataStorePreference.setValue("color", color)
    }
//
//    /**
//     * 获取列表动画模式
//     */
//    fun getListMode(): Int {
//        val kv = MMKV.mmkvWithID("app")
//        //0 关闭动画 1.渐显 2.缩放 3.从下到上 4.从左到右 5.从右到左
//        return kv.decodeInt("mode", 2)
//    }
//    /**
//     * 设置列表动画模式
//     */
//    fun setListMode(mode: Int) {
//        val kv = MMKV.mmkvWithID("app")
//         kv.encode("mode", mode)
//    }

    /**
     * 设置shap文件的颜色
     *
     * @param view
     * @param color
     */
    fun setShapColor(view: View, color: Int) {
        val drawable = view.background as GradientDrawable
        drawable.setColor(color)
    }

    /**
     * 设置shap的渐变颜色
     */
    fun setShapColor(view: View, color: IntArray, orientation: GradientDrawable.Orientation) {
        val drawable = view.background as GradientDrawable
        drawable.orientation = orientation//渐变方向
        drawable.colors = color//渐变颜色数组
    }

    /**
     * 设置selector文件的颜色
     *
     * @param view
     * @param yesColor
     * @param noColor
     */
    fun setSelectorColor(view: View, yesColor: Int, noColor: Int) {
        val mySelectorGrad = view.background as StateListDrawable
        try {
            val slDraClass = StateListDrawable::class.java
            val getStateCountMethod = slDraClass.getDeclaredMethod("getStateCount", *arrayOfNulls(0))
            val getStateSetMethod = slDraClass.getDeclaredMethod("getStateSet", Int::class.javaPrimitiveType)
            val getDrawableMethod = slDraClass.getDeclaredMethod("getStateDrawable", Int::class.javaPrimitiveType)
            val count = getStateCountMethod.invoke(mySelectorGrad) as Int//对应item标签
            for (i in 0 until count) {
                val stateSet = getStateSetMethod.invoke(mySelectorGrad, i) as IntArray//对应item标签中的 android:state_xxxx
                if (stateSet.isEmpty()) {
                    val drawable = getDrawableMethod.invoke(mySelectorGrad, i) as GradientDrawable//这就是你要获得的Enabled为false时候的drawable
                    drawable.setColor(yesColor)
                } else {
                    for (j in stateSet.indices) {
                        val drawable = getDrawableMethod.invoke(mySelectorGrad, i) as GradientDrawable//这就是你要获得的Enabled为false时候的drawable
                        drawable.setColor(noColor)
                    }
                }
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    suspend fun getColorStateList(context: Context): ColorStateList {
        val colors = intArrayOf(getColor(), ContextCompat.getColor(context, R.color.colorGray))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_checked, android.R.attr.state_checked)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }
    fun getColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color, ContextCompat.getColor(HhCpApp.context, R.color.colorGray))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_checked, android.R.attr.state_checked)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }

    suspend fun getOneColorStateList(): ColorStateList {
        val colors = intArrayOf(getColor())
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getOneColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color)
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }


    /**
     * 设置颜色透明一半
     * @param color
     * @return
     */
    fun translucentColor(color: Int): Int {
        val factor = 0.5f
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * 是否已经登录
     */
    suspend fun isLogin(): Boolean {
        return DataStorePreference.getBooleanValue("login",false)
    }

    /**
     * 设置是否已经登录
     */
    suspend fun setIsLogin(isLogin: Boolean) {
        DataStorePreference.setValue("login", isLogin)
    }

    /**
     * 获取token
     */
    suspend fun getToken(): String {
        return DataStorePreference.getStringValue("login","")
    }

    /**
     * 设置token
     */
    suspend fun setToken(token: String) {
        DataStorePreference.setValue("token", token)
    }

    /**
     * 是否是第一次登陆
     */
    suspend fun isFirst(): Boolean {
        return DataStorePreference.getBooleanValue("first")
    }

    /**
     * 是否是第一次登陆
     */
    suspend fun setFirst(first: Boolean) {
        DataStorePreference.setValue("first", first)
    }

    suspend fun setSearchHistoryData(searchResponseStr: String) {
        DataStorePreference.setValue("history", searchResponseStr)
    }

    /**
     * 获取搜索历史缓存数据
     */
    suspend fun getSearchHistoryData(): List<String> {
        val searchCacheStr =  DataStorePreference.getStringValue("history")
        if (!TextUtils.isEmpty(searchCacheStr)) {
            return Gson().fromJson(searchCacheStr
                , object : TypeToken<ArrayList<String>>() {}.type)
        }
        return arrayListOf()
    }
}
