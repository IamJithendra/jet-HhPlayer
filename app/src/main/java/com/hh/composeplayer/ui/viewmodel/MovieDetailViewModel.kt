package com.hh.composeplayer.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.hh.composeplayer.base.BaseViewModel
import com.hh.composeplayer.base.launch
import com.hh.composeplayer.bean.*
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.find

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.ui.viewmodel
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/16  13:15
 */
class MovieDetailViewModel : BaseViewModel() {
    //详情数据
    val movieBean = mutableStateOf(Video())

    //数据源
    val movieSource = mutableStateOf(PlayerSite())

    //选集列表
    val anthology = mutableStateListOf<VideoItem>()

    val episode = mutableStateOf(0)

    val collectFlag = mutableStateOf(false)

    var collectData = CollectBusK()

    fun getMovieDetail(ids : String){
        launch({
            repository.getMovieDetail(ids)
        },{
            it[0].let { v->
                LitePal.where("collectid = ?", v.id).find<CollectBusK>().let { cbk ->
                    if(cbk.isNotEmpty()){
                        collectFlag.value = cbk[0].flag
                    }
                }
                val moviedetail = v.dl?.dd
                if (moviedetail != null && moviedetail.size > 0) {
                    moviedetail.forEach { ddDTO ->
                        ddDTO.contentList = ddDTO.content?.split("#")
                    }
                    movieSource.value = (moviedetail[0])
                    episode.value = 0
                }
                movieBean.value = v
            }
            movieSource.value.contentList?.forEachIndexed { index, s ->
                anthology.add(VideoItem("", s, (index + 1).toString()))
            }
        },{
            showToast(it.message.toString())
        })
    }

    fun btnCollect() {
        if (collectFlag.value) {
            collectFlag.value = false
            LitePal.deleteAll<CollectBusK>( "collectId = ?", movieBean.value.id)
            showToast("取消收藏成功")
        } else {
            collectFlag.value = true
            collectData.cName = movieBean.value.name
            collectData.collectId = movieBean.value.id!!.toInt()
            collectData.flag = true
            collectData.type = movieBean.value.type
            collectData.saveOrUpdate( "collectId = ?", movieBean.value.id)
            showToast("收藏成功")
        }
    }
}