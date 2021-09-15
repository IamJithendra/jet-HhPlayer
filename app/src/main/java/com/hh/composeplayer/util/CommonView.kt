package com.hh.composeplayer.util

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/25  9:32
 */
@Composable
fun CpBottomBar(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Surface(
        elevation = 14.dp,
        color = MaterialTheme.colors.surface, // color will be adjusted for elevation
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp, 2.dp)
                .navigationBarsPadding()
            ,
            content = content
        )
    }
}

@Composable
fun ErrorBox(modifier: Modifier = Modifier,@StringRes titleId : Int) {
    Box(
        modifier, Alignment.Center
    ) {
        Text(stringResource(titleId))
    }
}
