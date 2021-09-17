package com.hh.composeplayer.ui

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.hh.composeplayer.MainActivity
import com.hh.composeplayer.R
import com.hh.composeplayer.ui.video.WebController
import com.hh.composeplayer.ui.viewmodel.MovieDetailViewModel
import com.hh.composeplayer.util.Mylog
import com.hh.composeplayer.util.SettingUtil
import com.hh.composeplayer.util.dp2px
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/16  9:39
 */
@Composable
fun MovieDetail(modifier:Modifier = Modifier,movieId : String = "") {
    Mylog.e("HHLog", "MovieDetail")
    val viewModel : MovieDetailViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    viewModel.let {
        LaunchedEffect(viewModel){
            withContext(IO){
                it.appColor = SettingUtil.getColor()
            }
            viewModel.getMovieDetail(movieId)
        }
    }
    val content = LocalContext.current

    Column(modifier.fillMaxSize()){
        Divider(modifier.statusBarsHeight().background(Color.Black))
        Mylog.e("HHLog", "MovieDetailColumn")
        AndroidView({
            val fragmentLayout = FrameLayout(it)
            fragmentLayout
        },
            modifier
                .fillMaxWidth()
                .height(200.dp)){
            coroutineScope.launch {
                if(viewModel.anthology.size>0){
                    WebController().loadUrl(content as MainActivity, viewModel.anthology[viewModel.episode.value].playUrl, it,this)
                    val back = ImageView(it.context)
                    back.setImageResource(com.glance.guolindev.R.drawable.material_ic_keyboard_arrow_left_black_24dp)
                    back.setColorFilter(android.graphics.Color.parseColor("#FFFFFF"))
                    val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT)
                    layoutParams.setMargins(it.dp2px(6),it.dp2px(6),0,0)
                    back.layoutParams = layoutParams
                    back.setPadding(it.dp2px(10),it.dp2px(10),it.dp2px(10),it.dp2px(10))
                    back.setOnClickListener {
                        viewModel.onBackPressed()
                    }
                    it.addView(back)
                }
            }

        }

        Text("${viewModel.movieBean.value.name}",fontSize = 20.sp,
            modifier = modifier.padding(top = 16.dp,start = 16.dp),
            color = colorResource(R.color.text_color),fontFamily = FontFamily.Monospace,fontWeight = FontWeight.Bold)

        Text("${viewModel.movieBean.value.area} ${viewModel.movieBean.value.year}", fontSize = 16.sp,modifier = modifier.padding(top = 10.dp,start = 16.dp))

        MovieDetailFavorite(viewModel = viewModel)

        MovieDetailAnthology(viewModel = viewModel)

        Text("简介:    ${viewModel.movieBean.value.des}",
            fontSize = 14.sp,modifier = modifier.padding(top = 20.dp,start = 16.dp))
    }
}

@Composable
fun MovieDetailFavorite(modifier: Modifier = Modifier,viewModel: MovieDetailViewModel) {
    Row (modifier.padding(top = 16.dp,start = 16.dp,end = 16.dp)){
        Mylog.e("HHLog", "MovieDetailRow")
        Text(stringResource(id = R.string.anthology), fontSize = 18.sp,fontWeight = FontWeight.Bold)
        Icon(if(viewModel.collectFlag.value)Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "collect",
            modifier = modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End)
                .clickable {
                    viewModel.btnCollect()
                }
            ,tint = Color(viewModel.appColor))
    }
}

@Composable
fun MovieDetailAnthology(modifier: Modifier = Modifier,viewModel: MovieDetailViewModel) {
    LazyRow (
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)){
        itemsIndexed(viewModel.anthology){index,it ->
            if(index == viewModel.episode.value){
                Box(
                    modifier
                        .padding(start = 6.dp, end = 6.dp)
                        .size(55.dp, 45.dp)
                        .border(1.dp, Color(viewModel.appColor), RoundedCornerShape(6.dp))
                        .clip(
                            RoundedCornerShape(6.dp)
                        )
                        .background(colorResource(id = R.color.white))
                        .clickable {
                            viewModel.episode.value = index
                        }
                ){
                    Text(it.item!!,textAlign = TextAlign.Center,modifier = modifier.align(Alignment.Center),color = Color(viewModel.appColor))
                }
            }
            else{
                Box(
                    modifier
                        .padding(start = 6.dp, end = 6.dp)
                        .size(55.dp, 45.dp)
                        .clip(
                            RoundedCornerShape(6.dp)
                        )
                        .background(colorResource(id = R.color.gray_light))
                        .clickable {
                            viewModel.episode.value = index
                        }
                ){
                    Text(it.item!!,textAlign = TextAlign.Center,modifier = modifier.align(Alignment.Center))
                }
            }
        }
    }
}
