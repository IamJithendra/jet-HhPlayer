package com.hh.composeplayer.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hh.composeplayer.R
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.ui.viewmodel.MineViewModel
import com.hh.composeplayer.util.*
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*


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
    LaunchedEffect(mineViewModel){
        withContext(IO){
            mineViewModel.appColor = SettingUtil.getColor()
        }
    }
    Column(modifier.fillMaxSize()) {
        MineTopAvatar(modifier,mineViewModel)
        Surface(
            modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .background(Color.White),
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface, // color will be adjusted for elevation
        ) {
            Column{
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
}

@Composable
fun MineItem(modifier: Modifier = Modifier,viewModel: MineViewModel,
             textName:String,icon: ImageVector,block:()->Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .height(45.dp)
            .clickable { block() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon, "$textName icon",
            modifier
                .size(28.dp)
                .padding(start = 10.dp), Color(viewModel.appColor)
        )
        Text(textName, modifier.padding(start = 10.dp),fontSize = 14.sp,fontFamily = FontFamily.Serif)
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
fun MineTopAvatar(modifier: Modifier = Modifier,viewModel: MineViewModel) {
    val context = LocalContext.current
    val fileBitmap by viewModel.fileBitmap.observeAsState()
    fileBitmap?.let {
        var bitmap by remember { mutableStateOf(it) }
        LaunchedEffect (it){
            withContext(IO){
                bitmap = BitmapBlur.doBlur(
                    Bitmap.createScaledBitmap(it, context.screenWidth, context.dp2px(340), true),
                    30, false,
                )
            }
        }
        Box(contentAlignment = Alignment.Center) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "me top background",
                    contentScale = ContentScale.FillWidth,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .clip(QureytoImageShapes(160f))
                )

                Column {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "me avatar",
                            contentScale = ContentScale.FillBounds,
                            modifier = modifier
                                .size(120.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .padding(3.dp)
                                .clip(
                                    CircleShape
                                )
                                .shadow(elevation = 150.dp, clip = true)
                                .clickable {
                                    XXPermissions
                                        .with(context)
                                        .permission(Permission.CAMERA)
                                        .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                                        .request(object : OnPermissionCallback {
                                            override fun onGranted(
                                                granted: List<String>,
                                                all: Boolean
                                            ) {
                                                if (all) {
                                                    viewModel.showPopup = true
                                                }
                                            }

                                            override fun onDenied(
                                                denied: List<String>,
                                                never: Boolean
                                            ) {
                                                if (never) {
                                                    viewModel.showToast(context.stringResource(R.string.permissions_cm_error))
                                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                                    XXPermissions.startPermissionActivity(
                                                        context,
                                                        denied
                                                    )
                                                } else {
                                                    viewModel.showToast(context.stringResource(R.string.permissions_camera_error))
                                                }
                                            }
                                        })

                                }
                        )

                    PicPopup(modifier, viewModel)
                }
            Spacer(modifier = modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.White,
                fontSize = 18.sp,
                modifier = modifier
                    .padding(bottom = 50.dp)
                    .align(Alignment.BottomCenter)
            )

        }
    }
}

@Composable
fun PicPopup(modifier: Modifier = Modifier,viewModel: MineViewModel) {
    val context = LocalContext.current
    val coroutineStore = rememberCoroutineScope()
    var f : File? = null
    // 定义一个申请系统权限的意图
    val photoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        f?.let { file ->
            if(file.exists()){
                HhUtil.createFile(context,file, context.stringResource(R.string.app_name))
                coroutineStore.launch {
                    withContext(IO){
                        //"${Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)}/${context.stringResource(R.string.app_name)}/${file.name}"
                        SettingUtil.setAvatarData("${Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)}/${context.stringResource(R.string.app_name)}/${file.name}")
                        viewModel.showPopup = false
                    }
                    file.delete()
                    viewModel.getAvatar()
                }
            }
        }
    }
    val pickLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.let {
            it.data?.also { uri ->
                viewModel.fileBitmap.value = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                coroutineStore.launch {
                    withContext(IO){
                        HhUtil.getPath2uri(context,uri)
                            ?.let { it1 -> SettingUtil.setAvatarData(it1) }
//                        SettingUtil.setAvatarData(uri.toString())
                        viewModel.showPopup = false
                    }
                }
            }
        }
    }
    DropdownMenu(
        modifier = modifier,
        expanded = viewModel.showPopup,
        onDismissRequest = {
            viewModel.showPopup = false
        },offset = DpOffset(20.dp, 0.dp)
    ) {
        DropdownMenuItem(onClick = {
            // 从相机中获得照片
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val imageFileName = String.format("JPEG_%s.jpg", HhUtil.formatDate(Date(),"yyyyMMdd_HHmmss"))
            f = File(context.getExternalFilesDir("$DIRECTORY_PICTURES/photo"), imageFileName)
            val photoUri = FileProvider.getUriForFile(context, context.packageName + ".provider", f!!)
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            intentCamera.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
            photoLauncher.launch(intentCamera)
        }) {
            Text(stringResource(R.string.photo))
        }
        DropdownMenuItem(onClick = {
            // 从相簿中获得照片
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            pickLauncher.launch(intent)
        }) {
            Text(stringResource(R.string.album))
        }
    }
}