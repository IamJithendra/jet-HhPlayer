package com.hh.composeplayer.util

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hh.composeplayer.HhCpApp
import com.hh.composeplayer.R
import com.hh.composeplayer.base.BaseActivity

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/31  11:41
 */
var isShowProgressDialog by mutableStateOf(false)
var progressDialogText by mutableStateOf("")

var boxProgress by mutableStateOf(false)
var boxProgressText by mutableStateOf("加载中...")

fun Context.showLd(title : String = resources.getString(R.string.loading)){
    progressDialogText = title
    isShowProgressDialog = true
}

fun Context.ldDismiss(){
    progressDialogText = resources.getString(R.string.loading)
    isShowProgressDialog = false
}

fun showLd(title : String = HhCpApp.context.resources.getString(R.string.loading)){
    progressDialogText = title
    isShowProgressDialog = true
}

fun ldDismiss(){
    progressDialogText = HhCpApp.context.resources.getString(R.string.loading)
    isShowProgressDialog = false
}

