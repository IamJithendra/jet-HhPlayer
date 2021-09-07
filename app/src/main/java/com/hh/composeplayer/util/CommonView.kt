package com.hh.composeplayer.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Row(
        modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(4.dp, 0.dp)
            .navigationBarsPadding(),
        content = content
    )
}
