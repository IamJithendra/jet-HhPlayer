package com.hh.composeplayer

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.accompanist.insets.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.hh.composeplayer.base.BaseActivity
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.manager.TabListWorkManager
import com.hh.composeplayer.ui.*
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

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        XXPermissions.with(this)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(granted: List<String>, all: Boolean) {
                    e("HHLog", "onGranted")
                    setContent {
                        MaterialTheme {
                            ProvideWindowInsets {
                                navHostController = rememberAnimatedNavController()
                                Scaffold(viewModel!!)
                                DialogProgress()
                            }
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
                    e("HHLog", "onDenied")
                    setContent {
                        MaterialTheme {
                            ProvideWindowInsets {
                                navHostController = rememberAnimatedNavController()
                                Scaffold(viewModel!!)
                                DialogProgress()
                            }
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

    private fun initWorkManager() {
        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)  // 网络状态
            .setRequiresBatteryNotLow(true)                 // 不在电量不足时执行
//            .setRequiresCharging(true)                      // 在充电时执行
            .setRequiresStorageNotLow(true)                 // 不在存储容量不足时执行
//        .setRequiresDeviceIdle(true)                    // 在待机状态下执行，需要 API 23
            .build()
        val request = PeriodicWorkRequest
            .Builder(TabListWorkManager::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(HhCpApp.context).enqueue(request)
    }
}


@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
private fun Scaffold(viewModel: MainViewModel) {
    e("HHLog", "Scaffold")
    Surface(
        color = MaterialTheme.colors.surface,
        contentColor = contentColorFor(MaterialTheme.colors.surface)
    ) {
        AnimatedNavHost(navController = navHostController, startDestination = Model.Main.name) {
            //当前需要展示首页/列表页
            composable(Model.Setting.toString()) {
                e("HHLog", "composableSetting")
                CpSetting()
            }
            composable(Model.Search.toString(),
                enterTransition = { _, _ ->
                    fadeIn(animationSpec = tween(700))
                }, exitTransition = { _, _ ->
                    fadeOut(animationSpec = tween(700))
                }
            ) {
                e("HHLog", "composableSearch")
                SearchView()
            }
            composable(Model.Main.toString()) {
                e("HHLog", "composableMain")
                MainContent(viewModel = viewModel)

            }
            composable(Model.About.toString()) {
                e("HHLog", "composableAbout")
                About()
            }
            composable(Model.Collect.toString()) {
                e("HHLog", "composableCollect")
                Collect()
            }
            composable(Model.Start.toString()) {
                e("HHLog", "composableProjcetAdress")
                ProjectAddress()
            }
            composable("${Model.MovieDetail}/{ids}",
                enterTransition = { _, _ ->
                    fadeIn(animationSpec = tween(1500))
                }, exitTransition = { _, _ ->
                    fadeOut(animationSpec = tween(1500))
                }
            ) {
                e("HHLog", "composableMovieDetail")
                val ids = it.arguments?.getString("ids", "")
                MovieDetail(movieId = ids!!)
            }
            composable(
                "${Model.SearchResult}/{searchName}",
                arguments = listOf(navArgument("searchName") {
                    // Make argument type safe
                    type = NavType.StringType
                }
                ),
            ) {
                e("HHLog", "composableSearchResult")
                val searchName = it.arguments?.getString("searchName", "")
                SearchResult(searchName = searchName!!)
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MainContent(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect("mainViewModel") {
        viewModel.appColor = SettingUtil.getColor()
    }
    Box {
        Column(modifier.fillMaxSize()) {
            HorizontalPager(
                state = viewModel.pagerState,
                modifier.weight(1f),
                dragEnabled = false
            ) { page ->
                when (page) {
                    0 -> {
                        e("HHLog", "MainContentHome")
                        Home()
                    }
                    1 -> {
                        e("HHLog", "MainContentMine")
                        Mine()
                    }
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
                }, modifier = Modifier.size(24.dp)
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
                }, modifier = Modifier.size(24.dp)
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




