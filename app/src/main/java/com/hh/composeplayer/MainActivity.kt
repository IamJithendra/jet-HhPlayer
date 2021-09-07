package com.hh.composeplayer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.hh.composeplayer.base.BaseActivity
import com.hh.composeplayer.base.parseState
import com.hh.composeplayer.bean.Jsons
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.bean.ResultState
import com.hh.composeplayer.ui.*
import com.hh.composeplayer.ui.theme.HelloComPoseTheme
import com.hh.composeplayer.util.*
import kotlinx.coroutines.launch
import com.hh.composeplayer.ui.viewmodel.HomeViewModel
import com.hh.composeplayer.ui.viewmodel.SearchViewModel
import com.hh.composeplayer.ui.viewmodel.SettingViewModel
import com.hh.composeplayer.util.CpNavigation.backAndReturnsIsLastPage
import com.hh.composeplayer.util.CpNavigation.navHostController
import com.hh.composeplayer.util.Mylog.e
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity<MainViewModel>() {
    var exitTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        XXPermissions.with(this)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(granted: List<String>, all: Boolean) {
                    e("HHLog","onGranted")
                    setContent {
                        HelloComPoseTheme {
                            navHostController = rememberNavController()
                            Scaffold(viewModel!!)
                            DialogProgress()
                        }
                    }
                }

                override fun onDenied(denied: List<String>, never: Boolean) {
                    if (never) {
                        Toast.makeText(this@MainActivity, "被永久拒绝授权，请手动授予存储权限", Toast.LENGTH_SHORT).show()
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@MainActivity, denied)
                    } else {
                        Toast.makeText(this@MainActivity, "获取存储权限失败", Toast.LENGTH_SHORT).show()
                    }
                    e("HHLog","onDenied")
                    setContent {
                        HelloComPoseTheme {
                            navHostController = rememberNavController()
                            Scaffold(viewModel!!)
                            DialogProgress()
                        }
                    }
                }
            })
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = NavController(this@MainActivity)
                if (nav.currentDestination != null && nav.currentDestination!!.id != androidx.appcompat.R.id.content) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        showToast("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}


@Composable
private fun Scaffold(viewModel: MainViewModel) {
    e("HHLog","Scaffold")
    val scaffoldState = rememberScaffoldState()//该脚手架的状态。
    ProvideWindowInsets {
//        val navController = rememberNavController()
        Scaffold(
            scaffoldState = scaffoldState,
            bottomBar = {
                if(CpNavigation.currentScreen == Model.Main){
                    MainBottomBar(viewModel,viewModel.pagerState.currentPage) {
                        viewModel.pagerState.currentPage = it
                    }
                }
            },
            content = { innerPadding ->
                NavHost(navController = navHostController, startDestination = Model.Main.name) {
//                    when (CpNavigation.currentScreen) {
                        //当前需要展示首页/列表页
                    composable(Model.Setting.toString()) {
                        e("HHLog","composableSetting")
                        CpSetting()
                    }
                    composable(Model.Search.toString()){
                        e("HHLog","composableSearch")
                        SearchView()
                    }
                    composable(Model.Main.toString()){
                        LaunchedEffect("mainViewModel"){
                            viewModel.appColor = SettingUtil.getColor()
                        }
                        e("HHLog","composableMain")
                        MainContent(viewModel = viewModel)
                    }
                }
            }
        )
    }
}

@Composable
private fun MainContent(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Pager(state = viewModel.pagerState, modifier.fillMaxSize()) {
        when (page) {
            0 -> {
                e("HHLog","MainContentHome")
                Home()
            }
            1 -> {
                e("HHLog","MainContentMine")
                Mine()}
        }
    }

}

@Composable
private fun MainBottomBar(viewModel: MainViewModel,selected: Int, currentChanged: (Int) -> Unit) {
    CpBottomBar {
        Column(
            Modifier
                .weight(1f)
                .clickable { currentChanged(0) }
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(id = R.drawable.ic_home_black_24dp),
                contentDescription = stringResource(id = R.string.main_title_home),
                tint = if (selected == 0) {
                    Color(viewModel.appColor)
                } else {
                    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                },modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = R.string.main_title_home),
                color = if (selected == 0) {
                    Color(viewModel.appColor)
                } else {
                    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                },
                fontSize = 12.sp
            )
        }
        Column(
            Modifier
                .weight(1f)
                .clickable { currentChanged(1) }
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = stringResource(id = R.string.main_title_mine),
                tint = if (selected == 1) {
                    Color(viewModel.appColor)
                } else {
                    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                },modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = R.string.main_title_mine),
                color = if (selected == 1) {
                    Color(viewModel.appColor)
                } else {
                    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                },
                fontSize = 12.sp
            )
        }
    }
}




