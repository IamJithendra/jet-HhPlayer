package com.hh.composeplayer.ui.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.hh.composeplayer.HhCpApp
import com.hh.composeplayer.R
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.util.SettingUtil
import com.hh.composeplayer.util.SingleLiveEvent


/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/2  10:50
 */
class MineViewModel : BaseViewModel() {
    val fileBitmap = SingleLiveEvent<Bitmap>()
    init {
        getAvatar()
    }

    var showPopup by mutableStateOf(false)

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getAvatar(){
        launch({
            SettingUtil.getAvatarData()
        }, {
            if(it == ""){
                fileBitmap.value = (HhCpApp.context.getDrawable(R.mipmap.icon_ava_hmbb) as BitmapDrawable).bitmap
            }
            else{
                fileBitmap.value = BitmapFactory.decodeFile(it)
            }
        },{
            showToast(it.message.toString())
            fileBitmap.value = (HhCpApp.context.getDrawable(R.mipmap.icon_ava_hmbb) as BitmapDrawable).bitmap
        })
    }
}