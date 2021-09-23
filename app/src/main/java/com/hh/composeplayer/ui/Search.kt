package com.hh.composeplayer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import com.google.accompanist.insets.ui.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.hh.composeplayer.R
import com.hh.composeplayer.ui.viewmodel.SearchViewModel
import com.hh.composeplayer.util.Mylog
import com.hh.composeplayer.util.SettingUtil
import com.hh.composeplayer.util.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/1  15:23
 */
@Composable
fun SearchView(modifier: Modifier = Modifier) {
    Mylog.e("HHLog", "SearchView")
    val searchViewModel: SearchViewModel = viewModel()
    LaunchedEffect(searchViewModel) {
        withContext(Dispatchers.IO) {
            searchViewModel.appColor = SettingUtil.getColor()
        }
        searchViewModel.getHistoryData()
    }
    Column(modifier.fillMaxSize()) {
        SearchTopBar(modifier, searchViewModel)
        SearchContent(modifier, searchViewModel)
    }
}

@Composable
fun SearchContent(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    Mylog.e("HHLog", "SearchContent")
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    Column {
        Row(
            modifier
                .fillMaxWidth()
                .padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.search_history),
                fontSize = 16.sp, color = Color(viewModel.appColor),
                modifier = modifier.padding(start = 12.dp)
            )
            Text(
                stringResource(id = R.string.close),
                color = colorResource(id = R.color.colorBlack666),
                fontSize = 14.sp,
                modifier = modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End)
                    .padding(end = 12.dp)
                    .clickable {
                        MaterialDialog(context)
                            .lifecycleOwner(lifecycleOwner)
                            .cancelable(false)
                            .show {
                                title(text = context.stringResource(R.string.tips))
                                message(text = context.stringResource(R.string.confirm_want_clear))
                                negativeButton(text = context.stringResource(R.string.cancel))
                                positiveButton(text = context.stringResource(R.string.close)) {
                                    //清空
                                    viewModel.clearHistoryData()
                                }
                                coroutineScope.launch {
                                    getActionButton(WhichButton.POSITIVE).updateTextColor(
                                        SettingUtil.getColor()
                                    )
                                    getActionButton(WhichButton.NEGATIVE).updateTextColor(
                                        SettingUtil.getColor()
                                    )
                                }
                            }
                    }
            )
        }
        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(top = 10.dp)) {
            items(viewModel.historyDataState) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.search(it)
                        }
                        .padding(top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        it,
                        fontSize = 15.sp, color = colorResource(id = R.color.text_color),
                        modifier = modifier.padding(start = 12.dp)
                    )
                    Icon(Icons.Filled.Close, contentDescription = "close history",
                        modifier = modifier
                            .weight(1f)
                            .size(18.dp, 18.dp)
                            .wrapContentWidth(Alignment.End)
                            .padding(end = 12.dp)
                            .clickable {
                                viewModel.removeIt(it)
                            },
                        tint = colorResource(id = R.color.colorBlack666)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTopBar(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    Mylog.e("HHLog", "SearchTopBar")
    val paddingValues = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars)
    TopAppBar(
        {
            TextField(
                value = viewModel.searchName, onValueChange = {
                    viewModel.searchName = it
                }, textStyle = TextStyle(fontSize = 15.sp),
                placeholder = {
                    Text(stringResource(id = R.string.please_key_search), fontSize = 15.sp)
                }, colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    placeholderColor = Color.Gray,
                    textColor = Color.White,
                    cursorColor = Color.Red
                ), modifier = modifier
                    .wrapContentHeight(Alignment.CenterVertically)
                    .height(IntrinsicSize.Min),
                trailingIcon = {
                    if (viewModel.searchName != "") {
                        IconButton(onClick = { viewModel.searchName = "" }) {
                            Icon(
                                Icons.Filled.Close, contentDescription = "close searchName",
                                tint = Color.White
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // 将键盘的回车键定义为搜索
                // 给回车键定义点击搜索事件，弹出搜索内容
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.search()
                }),
                singleLine = true
            )
        },
        backgroundColor = Color(viewModel.appColor),
        contentPadding = paddingValues,
        navigationIcon = {
            IconButton(onClick = { viewModel.onBackPressed() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = {
                viewModel.search()
            }) {
                Icon(
                    painterResource(id = R.mipmap.ic_search), contentDescription = "search",
                    tint = Color.White
                )
            }
        },
        elevation = 0.dp
    )
}