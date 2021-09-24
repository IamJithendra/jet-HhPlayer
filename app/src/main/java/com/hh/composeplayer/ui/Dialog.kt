package com.hh.composeplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hh.composeplayer.ui.theme.Purple500
import com.hh.composeplayer.util.*

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/30  16:17
 */
@Preview
@Composable
fun DialogProgress() {
    val dialogWidth = 160.dp
    val dialogHeight = 100.dp
    if (isShowProgressDialog) {
        Dialog(
            { isShowProgressDialog = false },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = true),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(dialogWidth)
                    .height(dialogHeight)
                    .background(White, shape = RoundedCornerShape(12.dp))
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    CircularProgressIndicator()
                    Text(progressDialogText,
                        Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun BoxProgress() {
    if(boxProgress){
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                val color = remember {
                    mutableStateOf(Purple500)
                }
                LaunchedEffect(color){
                    color.value = Color(SettingUtil.getColor())
                }
                CircularProgressIndicator(color = color.value)
                Text(boxProgressText,
                    Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp))
            }
        }
    }
}

@Composable
fun BoxProgressN(color: Color) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                CircularProgressIndicator(color = color)
                Text(boxProgressText,
                    Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp))
            }
        }
}
