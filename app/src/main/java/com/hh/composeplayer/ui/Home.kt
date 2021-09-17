package com.hh.composeplayer.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
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
        LaunchedEffect(homeViewModel) {
            it.appColor = SettingUtil.getColor()
            if (it.movieTabList.size == 0) {
                boxProgress = true
                homeViewModel.getMovieTabList()
            }
        }
    }
    Column(modifier.fillMaxSize()) {
            MainToolBar(modifier, homeViewModel)
            MovieTabLayout(modifier, homeViewModel)
            e("HHLog", "movieTabList"+homeViewModel.movieTabList.size)
            MovieListView(homeViewModel = homeViewModel)
    }
    BoxProgress()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MovieListView(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    e("HHLog", "MovieListView")
    homeViewModel.pagerState.pageCount = homeViewModel.movieTabList.size
    HorizontalPager(state = homeViewModel.pagerState, modifier.fillMaxSize()) { page ->
        when (page) {
            homeViewModel.movieTabList[page].pageId -> {
                HomeContent(modifier, homeViewModel, page)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    page: Int
) {
    e("HHLog", "HomeContent")
    val viewModel = viewModel(
        modelClass = MovieListViewModel::class.java,
        key = homeViewModel.movieTabList[page].staffId.toString()
    )
    val movieList = viewModel.getMovieList(page, homeViewModel.movieTabList[page].staffId)
        .collectAsLazyPagingItems()
    SwipeRefresh(rememberSwipeRefreshState(viewModel.isRefreshing), {
        e("HHLog", "rememberSwipeRefreshState")
        movieList.refresh()
    }) {
        SwipeRefreshItem(modifier, viewModel, movieList, movieList.itemCount)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeRefreshItem(
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel,
    movieList: LazyPagingItems<Video>,
    itemCount : Int
) {
    e("HHLog", "SwipeRefreshItem$itemCount")
    BoxWithConstraints {
        when(movieList.loadState.refresh){
            is LoadState.Error->{
                LazyColumn{
                    item {
                        ErrorBox(modifier
                            .height(maxHeight)
                            .width(maxWidth),R.string.no_network)
                    }
                }
            }
            is LoadState.Loading->{
                    Box(
                        modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column (horizontalAlignment = Alignment.CenterHorizontally){
                            CircularProgressIndicator()
                            Text(boxProgressText,
                                modifier.padding(0.dp, 8.dp, 0.dp, 0.dp))
                        }
                    }
            }
            else ->{
                when (itemCount) {
                    0 -> {
                        LazyColumn{
                            item {
                                ErrorBox(modifier
                                    .height(maxHeight)
                                    .width(maxWidth),R.string.no_data)
                            }
                        }
                    }
                    else -> {
                        LazyVerticalGrid(
                            cells = GridCells.Fixed(2),
                            modifier
                                .fillMaxSize()
                        ) {
                            e("HHLog", "LazyGridScope2$viewModel")
                            items(movieList.itemCount) {
                                e("HHLog", "LazyGridItemScope2$viewModel")
                                Card(
                                    modifier = modifier
                                        .padding(12.dp)
                                        .wrapContentHeight()
                                        .clickable { viewModel.startComposeBundle(Model.MovieDetail,movieList[it]!!.id.toString()) },
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = 5.dp
                                ) {
                                    Box(
                                        modifier = Modifier.height(IntrinsicSize.Max),
                                        propagateMinConstraints = true//传入的最小约束是否应传递给内容。
                                    ) {
                                        Image(
                                            painter = rememberCoilPainter(request = movieList[it]!!.pic,previewPlaceholder = R.drawable.ic_launcher_foreground),
                                            contentDescription = "Avatar",
                                            contentScale = ContentScale.FillBounds,
                                            modifier = modifier.height(200.dp)
                                        )
                                        //拉渐变
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    brush = Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            colorResource(id = R.color.font_primary)
                                                        ),
                                                        startY = 400f//数据越大黑色越少
                                                    )
                                                )
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.BottomStart
                                        )
                                        {
                                            Text(
                                                text = movieList[it]!!.name!!,
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
            if(viewModel.isShowError){
                IconButton(onClick = {
                    viewModel.getMovieTabList()
                }) {
                    Icon(
                        Icons.Filled.Refresh, contentDescription = "refresh",
                        tint = Color.White
                    )
                }
            }
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
                            it, viewModel.mainTopTabTextWidthList[viewModel.pagerState.currentPage]
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