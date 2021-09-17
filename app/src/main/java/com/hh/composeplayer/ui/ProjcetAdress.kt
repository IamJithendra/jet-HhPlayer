package com.hh.composeplayer.ui

import android.webkit.WebView
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hh.composeplayer.MainActivity
import com.hh.composeplayer.ui.video.WebController
import com.hh.composeplayer.ui.viewmodel.ProjectAdressViewModel
import com.hh.composeplayer.util.CpTopBar
import com.hh.composeplayer.util.SettingUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/17  13:42
 */

@Composable
fun ProjcetAdress(modifier: Modifier = Modifier) {
    val projcetAdressViewModel : ProjectAdressViewModel = viewModel()
    LaunchedEffect(projcetAdressViewModel){
        withContext(Dispatchers.IO){
            projcetAdressViewModel.appColor = SettingUtil.getColor()
        }
    }
    val content = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column {
        CpTopBar(viewModel = projcetAdressViewModel, title = "HhPlayer")
        AndroidView({
            val fragmentLayout = FrameLayout(it)
            fragmentLayout
        },modifier.fillMaxSize()){
            coroutineScope.launch {
                WebController().loadUrl(
                    content as MainActivity,
                    projcetAdressViewModel.projectAdress,
                    it,
                    this
                )
            }
        }
    }
}