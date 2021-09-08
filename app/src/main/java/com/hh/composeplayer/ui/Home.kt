package com.hh.composeplayer.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.hh.composeplayer.R
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.bean.Ty
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.ui.viewmodel.HomeViewModel
import com.hh.composeplayer.ui.viewmodel.MovieListViewModel
import com.hh.composeplayer.util.*
import com.hh.composeplayer.util.Mylog.e
import kotlinx.coroutines.launch

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/27  9:39
 */
@Composable
fun Home(modifier: Modifier = Modifier) {
    e("HHLog", "Home")
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
        }
    }
    BoxProgress()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MovieListView(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    homeViewModel.pagerState.pageCount = homeViewModel.movieTabList.size
    HorizontalPager(state = homeViewModel.pagerState,modifier.fillMaxSize()) {page ->
        when (page) {
            homeViewModel.movieTabList[page].pageId -> {
                HomeContent(modifier, homeViewModel,page)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class,ExperimentalPagerApi::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    page : Int
) {
    val viewModel = viewModel(
        modelClass = MovieListViewModel::class.java,
        key = homeViewModel.movieTabList[page].staffId.toString()
    )
    val movieList = remember {
        mutableStateListOf<Video>()
    }
    LaunchedEffect(homeViewModel.movieTabList[page].staffId) {
        viewModel.getMovieList(
            page,
            homeViewModel.movieTabList[page].staffId
        ).let {
            if (it.isNotEmpty()) {
                movieList.clear()
                movieList.addAll(it)
                viewModel.isShowError = false
            } else {
                viewModel.isShowError = true
            }
        }
    }
    SwipeRefresh(rememberSwipeRefreshState(viewModel.isRefreshing), {
        viewModel.movieListRefresh(
            page,
            homeViewModel.movieTabList[page].staffId
        )
    }) {
        if (movieList.size == 0) {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("没有数据")
            }
        } else {
            LazyVerticalGrid(cells = GridCells.Fixed(2), modifier.padding(bottom = 90.dp).fillMaxHeight()) {
                if (movieList.size > 0) {
                    items(movieList) {
                        Row {
                            Box(
                                modifier = modifier
                                    .weight(1f, fill = true)
                                    .padding(12.dp)
                                    .wrapContentHeight()
                                    .clickable {  viewModel.startCompose(Model.Search) }
                                ,
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
    e("HHLog", "MainToolBar")
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun MovieTabLayout(modifier: Modifier = Modifier, viewModel: HomeViewModel) {
    e("HHLog", "MovieTabLayout")
    Column {
        ScrollableTabRow(
            selectedTabIndex = viewModel.pagerState.currentPage,
            modifier = modifier.wrapContentWidth(),
            Color.White,
            indicator = {
                if (viewModel.movieTabList.size > 0 && viewModel.mainTopTabTextWidthList.size > 0) {
                    TabRowDefaults.Indicator(
                        modifier.pagerTabIndicatorOffsetH(
                            viewModel.pagerState,
                            it,viewModel.mainTopTabTextWidthList[viewModel.pagerState.currentPage]
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
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NewTabs(modifier: Modifier = Modifier, viewModel: HomeViewModel, titles: List<Ty>) {
    e("HHLog", "NewTabs")
    val tabList = remember { titles }
    val coroutineScope = rememberCoroutineScope()
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
            selected = viewModel.pagerState.currentPage == index,
            onClick = {
                coroutineScope.launch { viewModel.pagerState.animateScrollToPage(index) }
            },
            selectedContentColor = Color(viewModel.appColor),
            unselectedContentColor = colorResource(R.color.font_primary),
        )
    }
}