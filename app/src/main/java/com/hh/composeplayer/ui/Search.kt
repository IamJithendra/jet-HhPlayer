package com.hh.composeplayer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import com.google.accompanist.insets.ui.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.hh.composeplayer.R
import com.hh.composeplayer.ui.viewmodel.SearchViewModel
import com.hh.composeplayer.util.SettingUtil
import kotlinx.coroutines.Dispatchers
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
    val searchViewModel : SearchViewModel = viewModel()
    searchViewModel.let {
        LaunchedEffect("searchViewModel"){
            withContext(Dispatchers.IO){
                it.appColor = SettingUtil.getColor()
            }
        }
    }
    Column{
        SearchTopBar(modifier,searchViewModel)
        SearchContent(modifier,searchViewModel)
    }
}

@Composable
fun SearchContent(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    val itemList by viewModel.historyData.observeAsState()
    LazyColumn(modifier.fillMaxSize()){
        itemList?.let {
            items(it){
                Row(
                    modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.search_history),
                        fontSize = 16.sp,color = Color(viewModel.appColor),
                        modifier = modifier.padding(start = 12.dp)
                    )
                    Column(modifier.weight(1f)) {
                        Icon(Icons.Filled.Close, contentDescription = "close history",
                            modifier = modifier
                                .padding(end = 12.dp)
                                .align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTopBar(modifier: Modifier = Modifier, viewModel: SearchViewModel) {
    TopAppBar({
        TextField(value = viewModel.searchName, onValueChange = {
            viewModel.searchName = it
        },textStyle = TextStyle(fontSize = 15.sp),
            placeholder = {
            Text("请输入关键字搜索",fontSize = 15.sp)
        },colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            placeholderColor = Color.Gray,
            textColor = Color.White,
            cursorColor = Color.Red
        ),modifier = modifier.wrapContentHeight(Alignment.CenterVertically).height(IntrinsicSize.Min),
            trailingIcon = {
                if(viewModel.searchName!=""){
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
            keyboardActions = KeyboardActions(onSearch = { }) ,
            singleLine = true
        )
    },
        backgroundColor = Color(viewModel.appColor),
        contentPadding = rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars),
        navigationIcon = {
            IconButton(onClick = { viewModel.onBackPressed() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "back",tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = {  }) {
                Icon(
                    painterResource(id = R.mipmap.ic_search), contentDescription = "search",
                    tint = Color.White
                )
            }
        },
        elevation = 0.dp
    ) 
}