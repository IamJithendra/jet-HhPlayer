package com.hh.composeplayer

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.accompanist.insets.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.hh.composeplayer.base.BaseActivity
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.manager.TabListWorkManager
import com.hh.composeplayer.ui.*
import com.hh.composeplayer.ui.theme.HelloComPoseTheme
import com.hh.composeplayer.util.*
import kotlinx.coroutines.launch
import com.hh.composeplayer.util.CpNavigation.navHostController
import com.hh.composeplayer.util.Mylog.e
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.util.*
import java.util.concurrent.TimeUnit

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
                        showToast("被永久拒绝授权，请手动授予存储权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(this@MainActivity, denied)
                    } else {
                        showToast("获取存储权限失败")
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
        initWorkManager()
    }
    override fun onDestroy() {
        super.onDestroy()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    private fun initWorkManager(){
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        // 设置在大约 11:00:00 AM 执行
        dueDate.set(Calendar.HOUR_OF_DAY, 11)
        dueDate.set(Calendar.MINUTE, 0)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)  // 网络状态
            .setRequiresBatteryNotLow(true)                 // 不在电量不足时执行
            .setRequiresCharging(true)                      // 在充电时执行
            .setRequiresStorageNotLow(true)                 // 不在存储容量不足时执行
//        .setRequiresDeviceIdle(true)                    // 在待机状态下执行，需要 API 23
            .build()
        val request : OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(TabListWorkManager::class.java)
                .setConstraints(constraints)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
        WorkManager.getInstance(HhCpApp.context).enqueue(request)
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Scaffold(viewModel: MainViewModel) {
    e("HHLog","Scaffold")
    ProvideWindowInsets {
        Surface(
            color = MaterialTheme.colors.surface,
            contentColor = contentColorFor(MaterialTheme.colors.surface)
        ) {
            NavHost(navController = navHostController, startDestination = Model.Main.name) {
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
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainContent(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    Box{
        Column(modifier.fillMaxSize()) {
            HorizontalPager(state = viewModel.pagerState, modifier.weight(1f),dragEnabled = false) {page->
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
            MainBottomBar(viewModel) {
                coroutineScope.launch { viewModel.pagerState.animateScrollToPage(it) }
            }
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainBottomBar(viewModel: MainViewModel, currentChanged: (Int) -> Unit) {
    CpBottomBar {
        Column(
            Modifier
                .weight(1f)
                .clickable { currentChanged(0) }
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                painterResource(id = R.drawable.ic_home_black_24dp),
                contentDescription = stringResource(id = R.string.main_title_home),
                tint = if (viewModel.pagerState.currentPage == 0) {
                    Color(viewModel.appColor)
                } else {
                    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                },modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = R.string.main_title_home),
                color = if (viewModel.pagerState.currentPage == 0) {
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
                tint = if (viewModel.pagerState.currentPage == 1) {
                    Color(viewModel.appColor)
                } else {
                    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                },modifier = Modifier.size(24.dp)
            )
            Text(
                text = stringResource(id = R.string.main_title_mine),
                color = if (viewModel.pagerState.currentPage == 1) {
                    Color(viewModel.appColor)
                } else {
                    LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                },
                fontSize = 12.sp
            )
        }
    }
}




