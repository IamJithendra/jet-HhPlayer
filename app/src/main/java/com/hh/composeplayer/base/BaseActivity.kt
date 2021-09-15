/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hh.composeplayer.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hh.composeplayer.base.BaseViewModel.UIChangeLiveData.Companion.appExitEvent
import com.hh.composeplayer.base.BaseViewModel.UIChangeLiveData.Companion.dismissDialogEvent
import com.hh.composeplayer.base.BaseViewModel.UIChangeLiveData.Companion.onBackPressedEvent
import com.hh.composeplayer.base.BaseViewModel.UIChangeLiveData.Companion.showDialogEvent
import com.hh.composeplayer.base.BaseViewModel.UIChangeLiveData.Companion.showToastEvent
import com.hh.composeplayer.base.BaseViewModel.UIChangeLiveData.Companion.startComposeBundleEvent
import com.hh.composeplayer.base.BaseViewModel.UIChangeLiveData.Companion.startComposeEvent
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.util.CpNavigation
import com.hh.composeplayer.util.ldDismiss
import com.hh.composeplayer.util.showLd
import com.hh.composeplayer.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM : BaseViewModel?> : AppCompatActivity(), CoroutineScope by MainScope() {
    var viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        registorUIChangeLiveDataCallBack()
        //让ViewModel拥有View的生命周期感应
        viewModel?.let { lifecycle.addObserver(it) }
    }


    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    private fun initViewModel(): VM? {
        if (viewModel == null) {
            val modelClass: Class<out ViewModel>
            val type = javaClass.genericSuperclass
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[0] as Class<out ViewModel>
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                BaseViewModel::class.java
            }
            viewModel = createViewModel(this, modelClass) as VM
        }
        return viewModel
    }

    //注册ViewModel与View的契约UI回调事件
    private fun registorUIChangeLiveDataCallBack() {
        viewModel?.let {
            showDialogEvent .observe(this){toast ->
                showLd(toast)
            }
            dismissDialogEvent.observe(this){
                ldDismiss()
            }
            showToastEvent.observe(this){toast ->
                showToast(toast)
            }
            startComposeEvent.observe(this){model ->
                CpNavigation.to(model)
            }
            startComposeBundleEvent.observe(this){map->
                val model : Model = map[MODEL] as Model
                val any : Any = map[ANY] as Any
                CpNavigation.to(model,any)
            }
            onBackPressedEvent.observe(this){
                onBackPressed()
            }
            appExitEvent.observe(this){
                finish()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CpNavigation.backAndReturnsIsLastPage()
    }

    /**
     * 创建ViewModel
     */
    private fun <T : ViewModel> createViewModel(activity: AppCompatActivity, cls: Class<out T>): T {
        return ViewModelProvider(activity).get(cls)
    }
}
