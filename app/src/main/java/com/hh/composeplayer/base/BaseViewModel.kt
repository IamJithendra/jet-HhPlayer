package com.hh.composeplayer.base

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.hh.composeplayer.R
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.util.ServiceLocator
import com.hh.composeplayer.util.SingleLiveEvent


/**
 * @author huanghai
 * @date 2020/1/15
 * https://blog.csdn.net/ygzrsno/article/details/80117721  DataBinding onClick 的几种点击方式
 */

abstract class BaseViewModel : ViewModel(), LifecycleObserver {
    val repository = ServiceLocator.provideRepository()
    /**
     * 主题颜色 Theme Color
     */
    var appColor by mutableStateOf(R.color.font_primary)

    fun showDialog(title: String = "请稍后...") {
        UIChangeLiveData.showDialogEvent.postValue(title)
    }

    fun dismissDialog() {
        UIChangeLiveData.dismissDialogEvent.call()
    }

    fun showToast(title: String) {
        UIChangeLiveData.showToastEvent.postValue(title)
    }
    /**
     * 跳转页面
     */
    fun startCompose(model: Model) {
        UIChangeLiveData.startComposeEvent.postValue(model)
    }
    /**
     * 跳转页面带参数
     */
    fun startComposeBundle(model: Model,any: Any) {
        val params: MutableMap<String, Any> = HashMap()
        params[MODEL] = model
        params[ANY] = any
        UIChangeLiveData.startComposeBundleEvent.postValue(params)
    }
    /**
     * 返回上一层
     */
    fun onBackPressed() {
        UIChangeLiveData.onBackPressedEvent.call()
    }

    fun onExit(){
        UIChangeLiveData.appExitEvent.call()
    }

    override fun onCleared() {
        super.onCleared()
        //ViewModel销毁时会执行，同时取消所有异步任务
    }

    class UIChangeLiveData : SingleLiveEvent<Any?>() {
        companion object{
            var showDialogEvent: SingleLiveEvent<String> = SingleLiveEvent()
            var showToastEvent: SingleLiveEvent<String> = SingleLiveEvent()
            var startComposeEvent: SingleLiveEvent<Model> = SingleLiveEvent()
            var startComposeBundleEvent: SingleLiveEvent<MutableMap<String,Any>> = SingleLiveEvent()
            var dismissDialogEvent: SingleLiveEvent<Void> = SingleLiveEvent()
            var onBackPressedEvent: SingleLiveEvent<Void> = SingleLiveEvent()
            var appExitEvent: SingleLiveEvent<Void> = SingleLiveEvent()
        }
    }
}

var MODEL = "MODEL"
var ANY = "ANY"