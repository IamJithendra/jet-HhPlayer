package com.hh.composeplayer.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hh.composeplayer.R
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.ui.viewmodel.MineViewModel
import com.hh.composeplayer.util.*
import com.hh.composeplayer.util.CpNavigation.navHostController
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/27  9:40
 */
@Composable
 fun Mine(modifier: Modifier = Modifier) {
    val mineViewModel: MineViewModel = viewModel()
    mineViewModel.let {
        LaunchedEffect("mineViewModel"){
            withContext(IO){
                it.appColor = SettingUtil.getColor()
            }
        }
    }
    Column(
        modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB)),
    ) {
        MineTopAvater()
        Column(
            modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ){
            MineItem(modifier,mineViewModel,stringResource(R.string.main_mine_start),
                Icons.Filled.Star){ mineViewModel.startCompose(Model.Start) }
            Divider(modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp))
            MineItem(modifier,mineViewModel,stringResource(R.string.main_mine_collect),
                Icons.Filled.Favorite){ mineViewModel.startCompose(Model.Collect) }
            Divider(modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp))
            MineItem(modifier ,mineViewModel,stringResource(R.string.main_mine_setting),
                Icons.Filled.Settings){
                mineViewModel.startCompose(Model.Setting)
            }
            Divider(modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp))
            MineItem(modifier ,mineViewModel,stringResource(R.string.main_mine_about),
                Icons.Filled.AccountCircle
            ) { mineViewModel.startCompose(Model.About) }
        }
    }
}

@Composable
fun MineItem(modifier: Modifier = Modifier,viewModel: MineViewModel,
             textName:String,icon: ImageVector,block:()->Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(45.dp)
            .clickable { block() }
            ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon, "$textName icon",
            modifier
                .size(28.dp)
                .padding(start = 10.dp), Color(viewModel.appColor)
        )
        Text(textName, modifier.padding(start = 10.dp))
        Icon(
            Icons.Filled.KeyboardArrowRight,textName,
            modifier
                .weight(1f)
                .padding(end = 10.dp)
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Composable
fun MineTopAvater() {
    val context = LocalContext.current
    val imgResources = ImageBitmap.imageResource(R.mipmap.icon_ava_hmbb)
    var bitmap by remember {
        mutableStateOf(imgResources)
    }
    LaunchedEffect (imgResources){
        withContext(IO){
            bitmap = BitmapBlur.doBlur(
                BitmapFactory.decodeResource(context.resources, R.mipmap.icon_ava_hmbb),
                50, false,
            ).asImageBitmap()
        }
    }
    Box(contentAlignment = Alignment.Center) {
        Image(
            bitmap = bitmap,
            contentDescription = "me top background",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(QureytoImageShapes(160f))
        )
//        Box(
//            contentAlignment = Alignment.Center,
//            modifier = Modifier
//                .clip(CircleShape)
//                .background(Color(250, 250, 250, 121))
//                .width(150.dp)
//                .height(150.dp)
//        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.mipmap.icon_ava_hmbb),
                contentDescription = "me avatar",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(120.dp)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(3.dp)
                    .clip(
                        CircleShape
                    )
                    .shadow(elevation = 150.dp, clip = true)
            )
//        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .align(Alignment.BottomCenter)
        )
    }
}