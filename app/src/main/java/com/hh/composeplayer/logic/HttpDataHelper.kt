package com.hh.composeplayer.logic

import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.hh.composeplayer.api.ApiService
import com.hh.composeplayer.api.TaskApi
import com.hh.composeplayer.bean.*
import com.hh.composeplayer.util.Mylog
import com.hh.composeplayer.util.xmlToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import org.litepal.LitePal
import org.litepal.extension.find


/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.logic
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/16  15:31
 */
class HttpDataHelper {
    suspend fun getTabList(): List<Ty> {
        val tabListResult: MutableList<Ty> = ArrayList()
        val tabList = TaskApi.create(ApiService::class.java).getPlayer()
        val jsonObject = xmlToJson(tabList.toString())?.toJson()
        val homeTab = Gson().fromJson(jsonObject.toString(), Jsons::class.java)
        homeTab.apply {
            mJson = jsonObject.toString()
            saveOrUpdate("id = ?", id.toString())
            rss?.apply {
                saveOrUpdate("id = ?", id.toString())
                `class`?.apply {
                    saveOrUpdate("id = ?", id.toString())
                    ty?.apply {
                        tabListResult.add(Ty("最新"))
                        tabListResult.addAll(this)
                        forEachIndexed { index, ty ->
                            ty.pageId = (index + 1)
                            ty.saveOrUpdate("id = ?", (index + 1).toString())
                        }
                    }
                }
            }
        }
        return tabListResult
    }

    suspend fun getPlayerList(state: Long, page: Int): ListVideo {
        var movieList = ListVideo()
        val movieStr = if (state == 0L) {
            TaskApi.create(ApiService::class.java).getPlayerListZx(page)
        } else {
            TaskApi.create(ApiService::class.java).getPlayerList(state.toString(), page)
        }
        val jsonObject = xmlToJson(movieStr.toString())?.toJson()
        val movieBean = JSON.parseObject(jsonObject.toString(), MovieBean::class.java)
        movieBean.rss?.run {
            list?.apply {
                if (video == null) {
                    video = ArrayList()
                }
                movieList = this
            }
        }
        return movieList
    }

    suspend fun getSearchResultList(searchName: String, page: Int): List<Video> {
        val searchList: MutableList<Video> = ArrayList()
        val movieStr = TaskApi.create(ApiService::class.java).getSearchPlayerList(searchName, page)
        val jsonObject = xmlToJson(movieStr.toString())?.toJson()
        val movieBean = JSON.parseObject(jsonObject.toString(), MovieBean::class.java)
        movieBean.rss?.run {
            list?.apply {
                video?.apply {
                    searchList.addAll(this)
                }
            }
        }
        return searchList
    }

    suspend fun getMovieDetail(ids: String): List<Video> {
        val movieDetail: MutableList<Video> = ArrayList()
        val movieDetailStr = TaskApi.create(ApiService::class.java).getPlayerDetail(ids)
        val jsonObject = xmlToJson(movieDetailStr.toString())?.toJson()
        val movieBean = JSON.parseObject(jsonObject.toString(), MovieBean::class.java)
        movieBean.rss?.run {
            list?.apply {
                video?.apply {
                    movieDetail.addAll(this)
                }
            }
        }
        return movieDetail
    }
}