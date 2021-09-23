package com.hh.composeplayer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hh.composeplayer.R
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.ui.viewmodel.AboutViewModel
import com.hh.composeplayer.util.CpTopBar
import com.hh.composeplayer.util.SettingUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/16  8:50
 */
@Composable
fun About(modifier: Modifier = Modifier) {
    val aboutViewModel: AboutViewModel = viewModel()
    LaunchedEffect(aboutViewModel) {
        withContext(Dispatchers.IO) {
            aboutViewModel.appColor = SettingUtil.getColor()
        }
    }
    Column(modifier.fillMaxSize()) {
        CpTopBar(viewModel = aboutViewModel, title = stringResource(R.string.main_mine_about))
        AboutItem(textTitle = stringResource(R.string.movie_api),textContent = aboutViewModel.apiUrl)
        AboutItem(textTitle = stringResource(R.string.about_email),textContent = aboutViewModel.emailAddress)
        AboutItem(textTitle = stringResource(R.string.project_address),textContent = aboutViewModel.projectAddress,block = {
            aboutViewModel.startCompose(Model.Start)
        })
    }
}

@Composable
fun AboutItem(modifier: Modifier = Modifier,textTitle : String,textContent : String,block:()->Unit = {}) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable {
                block()
            }
            .padding(15.dp)) {
        Text(
            textTitle,
            color = colorResource(R.color.text_color),
            fontSize = 15.sp
        )
        Text(
            textContent,
            color = colorResource(R.color.forum_inactive_color),
            fontSize = 14.sp
        )
    }
    Divider(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
    )
}