package com.hh.composeplayer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hh.composeplayer.R
import com.hh.composeplayer.bean.CollectBusK
import com.hh.composeplayer.bean.Model
import com.hh.composeplayer.ui.viewmodel.CollectViewModel
import com.hh.composeplayer.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.findAll

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/17  10:48
 */
@Composable
fun Collect(modifier: Modifier = Modifier) {
    val collectViewModel: CollectViewModel = viewModel()
    LaunchedEffect(collectViewModel) {
        if(collectViewModel.collectData.size == 0){
            collectViewModel.collectData.addAll(withContext(Dispatchers.IO) {
                collectViewModel.appColor = SettingUtil.getColor()
                LitePal.findAll<CollectBusK>()
            })
        }
    }
    Column(modifier.fillMaxSize()) {
        CpTopBar(modifier, collectViewModel, stringResource(R.string.main_mine_collect))
        CollectContent(modifier, collectViewModel)
    }
}

@Composable
fun CollectContent(modifier: Modifier = Modifier, viewModel: CollectViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    BoxWithConstraints{
    LazyColumn(modifier.fillMaxSize()) {
            when (viewModel.collectData.size) {
                0 -> {
                    item {
                        ErrorBox(
                            modifier
                                .height(maxHeight)
                                .width(maxWidth), R.string.no_data
                        )
                    }
                }
                else -> {
                    items(viewModel.collectData) {
                        Row(
                            modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.apply {
                                        viewModel.startComposeBundle(
                                            Model.MovieDetail,
                                            it.collectId.toString()
                                        )
                                    }
                                }
                                .padding(top = 10.dp, bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "${it.cName}  ${it.type}",
                                fontSize = 15.sp, color = colorResource(id = R.color.text_color),
                                modifier = modifier.padding(start = 12.dp)
                            )
                            Icon(
                                Icons.Filled.Delete, contentDescription = "close history",
                                modifier = modifier
                                    .weight(1f)
                                    .wrapContentWidth(Alignment.End)
                                    .padding(end = 12.dp)
                                    .clickable {
                                        context.showMessage(
                                            "确定要取消收藏吗?",
                                            positiveButtonText = "确定",
                                            negativeButtonText = "取消",
                                            positiveAction = {
                                                coroutineScope.launch {
                                                    withContext(IO) {
                                                        LitePal.deleteAll<CollectBusK>(
                                                            "collectId = ?",
                                                            it.collectId.toString()
                                                        )
                                                    }
                                                    viewModel.collectData.remove(it)
                                                    viewModel.showToast("取消收藏成功")
                                                }
                                            })
                                    },
                                tint = colorResource(id = R.color.colorBlack666)
                            )
                        }
                        Divider(modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}