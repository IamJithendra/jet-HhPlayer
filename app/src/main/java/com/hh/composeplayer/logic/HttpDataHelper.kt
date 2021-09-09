package com.hh.composeplayer.logic

import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.hh.composeplayer.api.ApiService
import com.hh.composeplayer.api.TaskApi
import com.hh.composeplayer.bean.Jsons
import com.hh.composeplayer.bean.MovieBean
import com.hh.composeplayer.bean.Ty
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.util.boxProgress
import com.hh.composeplayer.util.xmlToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.logic
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/16  15:31
 */
class HttpDataHelper {
    suspend fun getTabList(coroutineScope: CoroutineScope): List<Ty> {
        val tabListr: MutableList<Ty> = ArrayList()
        val job = coroutineScope.async(IO) {
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
                            tabListr.add(Ty("最新"))
                            tabListr.addAll(this)
                            forEachIndexed { index, ty ->
                                ty.pageId = (index+1)
                                ty.saveOrUpdate("id = ?", (index + 1).toString())
                            }
                        }
                    }
                }
            }
            tabListr
        }
        return job.await()
    }

    suspend fun getPlayerList(state: Long, page : Int, coroutineScope: CoroutineScope):List<Video> {
        val movieList: MutableList<Video> = ArrayList()
        val job = coroutineScope.async(IO) {
//            runCatching {
//                boxProgress = true
                val movieStr = if (state == 0L) {
                    TaskApi.create(ApiService::class.java).getPlayerListZx(page)
                } else {
                    TaskApi.create(ApiService::class.java).getPlayerList(state.toString(), page)
                }
                val jsonObject = xmlToJson(movieStr.toString())?.toJson()
                val movieBean = JSON.parseObject(jsonObject.toString(), MovieBean::class.java)
                movieBean.rss?.run {
                    list?.apply {
                        video?.apply {
                            movieList.addAll(this)
                        }
                    }
                }
                movieList
//            }.onSuccess {
//                boxProgress = false
//            }.onFailure {
//                boxProgress = false
//            }
        }
        return job.await()
    }
}