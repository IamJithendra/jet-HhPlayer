package com.hh.composeplayer.util

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.ui.theme.shimmerHighLight
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.palette.BitmapPalette

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

@Composable
fun CpTopBar(modifier: Modifier = Modifier,viewModel : BaseViewModel,title : String){
    TopAppBar(
        {
            Text(title, color = Color.White)
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


@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    bitmapPalette: BitmapPalette? = null,
    highlightColor : Color = shimmerHighLight
) {
    CoilImage(
        imageModel = url,
        modifier = modifier,
        contentScale = contentScale,
        circularReveal = CircularReveal(duration = 250),
        bitmapPalette = bitmapPalette,
        shimmerParams = ShimmerParams(
            baseColor = MaterialTheme.colors.background,
            highlightColor = highlightColor,
            dropOff = 0.65f
        ),
        failure = {
            Box(modifier.fillMaxSize()){
                Text(
                    text = "image request failed.",
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    modifier = modifier.align(Alignment.Center)
                )
            }
        }
    )
}