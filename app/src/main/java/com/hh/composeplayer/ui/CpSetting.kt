package com.hh.composeplayer.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.color.colorChooser
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.hh.composeplayer.R
import com.hh.composeplayer.ui.viewmodel.SettingViewModel
import com.hh.composeplayer.util.ColorUtil.ACCENT_COLORS
import com.hh.composeplayer.util.ColorUtil.PRIMARY_COLORS_SUB
import com.hh.composeplayer.util.SettingUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/26  9:50
 */

@SuppressLint("CheckResult")
@Composable
fun CpSetting(modifier: Modifier = Modifier){
    val lifecycleCoroutineScope = rememberCoroutineScope()
    val content = LocalContext.current
    val settingViewModel : SettingViewModel = viewModel()
    settingViewModel.let {
        LaunchedEffect("settingViewModel"){
            withContext(IO){
                it.appColor = SettingUtil.getColor()
            }
        }
    }
    Column {
        SettingTopBar(modifier,settingViewModel)
        Text(stringResource(R.string.setting_basic_text),
            color = Color(settingViewModel.appColor),
            fontSize = 13.sp,modifier = modifier.padding(15.dp))
        Column(
            modifier
                .padding(15.dp)
                .clickable { }) {
            Text(stringResource(R.string.setting_clear_cache),color = colorResource(R.color.text_color),fontSize = 15.sp)
            Text(settingViewModel.caCheSize.toString(),color = colorResource(R.color.forum_inactive_color),fontSize = 14.sp)
        }
        Column(
            modifier
                .padding(15.dp)
                .clickable { }) {
            Text(stringResource(R.string.exit),color = colorResource(R.color.text_color),fontSize = 15.sp)
            Text(stringResource(R.string.exit_app),color = colorResource(R.color.forum_inactive_color),fontSize = 14.sp)
        }
        Divider(modifier = modifier
            .height(1.dp)
            .fillMaxWidth())
        Text(stringResource(R.string.setting_other_text),
            color = Color(settingViewModel.appColor),
            fontSize = 13.sp,modifier = modifier.padding(15.dp))
        Row(
            modifier
                .clickable {
                    MaterialDialog(content).show {
                        title(R.string.setting_theme_color)
                        colorChooser(
                            ACCENT_COLORS,
                            initialSelection = settingViewModel.appColor,
                            subColors = PRIMARY_COLORS_SUB
                        ) { _, color ->
                            settingViewModel.appColor = color
                            lifecycleCoroutineScope.launch {
                                withContext(IO) {
                                    ///修改颜色
                                    SettingUtil.setColor(color)
                                }
                            }
                        }
                        getActionButton(WhichButton.POSITIVE).updateTextColor(
                            settingViewModel.appColor
                        )
                        getActionButton(WhichButton.NEGATIVE).updateTextColor(
                            settingViewModel.appColor
                        )
                        positiveButton(R.string.done)
                        negativeButton(R.string.cancel)
                    }
                }
                .padding(15.dp)) {
            Column{
                Text(stringResource(R.string.setting_theme_color),color = colorResource(R.color.text_color),fontSize = 15.sp)
                Text(stringResource(R.string.setting_theme_color_tips),color = colorResource(R.color.forum_inactive_color),fontSize = 14.sp)
            }
            Column (
                modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp)){
                Spacer(modifier
                    .size(48.dp)
                    .border(1.dp, Color.Black, CircleShape)
                    .clip(CircleShape)
                    .background(Color(settingViewModel.appColor))
                    .align(Alignment.End))
            }
        }
    }
}

@Composable
fun SettingTopBar(modifier: Modifier = Modifier,viewModel : SettingViewModel){
    TopAppBar(
        {
            Text("设置", color = Color.White)
        },
        modifier = modifier,
        backgroundColor = Color(viewModel.appColor),
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars),
        navigationIcon = {
            IconButton(
                onClick = {
                    viewModel.onBackPressed()
                }
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back",tint = Color.White)
            }
        },
        elevation = 0.dp,
    )
}