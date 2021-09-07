package com.hh.composeplayer.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hh.composeplayer.R
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.bean.Ty
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.ui.viewmodel.HomeViewModel
import com.hh.composeplayer.ui.viewmodel.MovieListViewModel
import com.hh.composeplayer.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/27  9:39
 */
@Composable
fun Home(modifier: Modifier = Modifier) {
    Mylog.e("HHLog", "Home")
    val homeViewModel: HomeViewModel = viewModel()
    homeViewModel.let {
        if (it.movieTabList.size == 0) {
            homeViewModel.getMovieTabList()
        }
        LaunchedEffect(homeViewModel) {
            it.appColor = SettingUtil.getColor()
        }
    }
    Column(modifier.fillMaxSize()) {
        MainToolBar(modifier, homeViewModel)
        MovieTabLayout(modifier, homeViewModel)
        if (homeViewModel.movieTabList.size > 0) {
            MovieListView(homeViewModel = homeViewModel)
            LaunchedEffect(homeViewModel.movieTabList){
                homeViewModel.getMovieTabList()
            }
        }
    }
    BoxProgress()
}

@Composable
fun MovieListView(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    homeViewModel.pagerState.maxPage = homeViewModel.movieTabList.size
    Pager(homeViewModel.pagerState) {
        when (currentPage) {
            homeViewModel.movieTabList[homeViewModel.mainTopTabState].staffId.toInt() -> {
                val viewModel = viewModel(
                    modelClass = MovieListViewModel::class.java,
                    key = homeViewModel.mainTopTabState.toString()
                )
                LaunchedEffect(currentPage) {
                    viewModel.getMovieList(
                        homeViewModel.mainTopTabState,
                        homeViewModel.movieTabList[homeViewModel.mainTopTabState].staffId
                    ).let {
                        if (it.isNotEmpty()) {
                            viewModel.movieList.value!!.clear()
                            viewModel.movieList.value!!.addAll(it)
                            viewModel.isShowError = false
                        } else {
                            viewModel.isShowError = true
                        }

                    }
                }
                HomeContent1(modifier, viewModel, homeViewModel)
            }
        }
    }

}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    innerPadding: PaddingValues
) {

    SwipeRefresh(rememberSwipeRefreshState(false), {
    }) {
        val movieList = remember {
            mutableStateListOf<Video>()
        }
//        LaunchedEffect(viewModel.movieList){
//            movieList.addAll(viewModel.getMovieList())
//        }
        //要分为几列
        val nColumns = 2
        //rows 总共几行
        val rows = (movieList.size + nColumns - 1) / nColumns
        Column(modifier.fillMaxSize()) {
            LazyColumn(
                contentPadding = innerPadding,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                // 列表反向显示
//                    reverseLayout = true
            ) {
                items(rows) {
                    Row {
                        for (columnIndex in 0 until nColumns) {
                            //itemIndex List数据位置
                            val itemIndex = it * 2 + columnIndex
                            if (itemIndex < movieList.size) {
                                Box(
                                    modifier = modifier
                                        .weight(1f, fill = true)
                                        .padding(12.dp)
                                        .wrapContentHeight(),
                                    propagateMinConstraints = true,
                                ) {
                                    Image(
                                        painter = rememberCoilPainter(request = movieList[it].pic),
                                        contentDescription = "avater",
                                        contentScale = ContentScale.FillHeight,
                                        modifier = modifier.height(200.dp)
                                    )
                                    Text(
                                        text = movieList[it].name!!,
                                        modifier
                                            .height(40.dp)
                                            .background(
                                                colorResource(id = R.color.translucent_5)
                                            )
                                            .padding(top = 10.dp)
                                            .align(Alignment.BottomCenter),
                                        textAlign = TextAlign.Center,
                                        fontSize = 13.sp,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Medium,
                                        textDecoration = TextDecoration.None
                                    )
                                }
                            } else {
                                Spacer(modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent1(
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel,
    homeViewModel: HomeViewModel
) {
    Mylog.e("HHLog", "HomeContent1")
    SwipeRefresh(rememberSwipeRefreshState(viewModel.isRefreshing), {
        viewModel.movieListRefresh(
            homeViewModel.mainTopTabState,
            homeViewModel.movieTabList[homeViewModel.mainTopTabState].staffId
        )
    }) {
        val movieList = viewModel.movieList.observeAsState()
        if (viewModel.isShowError) {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("没有数据")
            }
        } else {
            LazyVerticalGrid(cells = GridCells.Fixed(2), modifier.padding(bottom = 90.dp)) {
                if (movieList.value!!.size > 0) {
                    items(movieList.value!!) {
                        Row {
                            Box(
                                modifier = modifier
                                    .weight(1f, fill = true)
                                    .padding(12.dp)
                                    .wrapContentHeight(),
                                propagateMinConstraints = true,
                            ) {
                                Image(
                                    painter = rememberCoilPainter(request = it.pic),
                                    contentDescription = "avater",
                                    contentScale = ContentScale.FillBounds,
                                    modifier = modifier.height(200.dp)
                                )
                                Text(
                                    text = it.name!!,
                                    modifier
                                        .height(40.dp)
                                        .background(
                                            colorResource(id = R.color.translucent_5)
                                        )
                                        .padding(top = 10.dp)
                                        .align(Alignment.BottomCenter),
                                    textAlign = TextAlign.Center,
                                    fontSize = 13.sp,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Medium,
                                    textDecoration = TextDecoration.None
                                )
                            }
                        }
                    }
                }
                else{
                    item {
                        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("没有数据")
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun MainToolBar(modifier: Modifier = Modifier, viewModel: HomeViewModel) {
    Mylog.e("HHLog", "MainToolBar")
    TopAppBar(
        { Text(stringResource(id = R.string.main_title_home), color = Color.White) },
        modifier = modifier,
        backgroundColor = Color(viewModel.appColor),
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars),
        actions = {
            IconButton(onClick = {
                viewModel.startCompose(Model.Search)
            }) {
                Icon(
                    painterResource(id = R.mipmap.ic_search), contentDescription = "search",
                    tint = Color.White
                )
            }
        },
        elevation = 0.dp,
    )
}

@Composable
private fun MovieTabLayout(modifier: Modifier = Modifier, viewModel: HomeViewModel) {
    Mylog.e("HHLog", "MovieTabLayout")
    Column {
        ScrollableTabRow(
            selectedTabIndex = viewModel.mainTopTabState,
            modifier = modifier.wrapContentWidth(),
            Color.White,
            indicator = {
                if (viewModel.movieTabList.size > 0 && viewModel.mainTopTabTextWidthList.size > 0) {
                    TabRowDefaults.Indicator(
                        modifier.tabIndicatorOffsetH(
                            it[viewModel.mainTopTabState],
                            viewModel.mainTopTabTextWidthList[viewModel.mainTopTabState]
                        ),
                        2.dp,
                        Color(viewModel.appColor)
                    )
                }
            },
            edgePadding = 1.dp//开始和结束边缘与其内部选项卡之间的距离
        ) {
            NewTabs(modifier, viewModel, viewModel.movieTabList)
        }
//        Spacer(modifier = modifier.height(10.dp))
    }
}

@Composable
fun NewTabs(modifier: Modifier = Modifier, viewModel: HomeViewModel, titles: List<Ty>) {
    Mylog.e("HHLog", "NewTabs")
    val tabList = remember { titles }
    tabList.forEachIndexed { index, title ->
        Tab(
            text = {
                Text(title.content!!, modifier = modifier.layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width, placeable.height) {
                        viewModel.mainTopTabTextWidthList.add(placeable.width.toDp())
                        placeable.placeRelative(0, 0)
                    }
                }
                )
            },
            selected = viewModel.mainTopTabState == index,
            onClick = {
                viewModel.mainTopTabState = index
                viewModel.pagerState.currentPage = index
            },
            selectedContentColor = Color(viewModel.appColor),
            unselectedContentColor = colorResource(R.color.font_primary),
        )
    }
}