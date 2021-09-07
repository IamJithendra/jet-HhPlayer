package com.hh.composeplayer.logic

import com.google.gson.Gson
import com.hh.composeplayer.api.ApiService
import com.hh.composeplayer.api.TaskApi
import com.hh.composeplayer.bean.Jsons
import com.hh.composeplayer.bean.MovieBean
import com.hh.composeplayer.bean.Ty
import com.hh.composeplayer.bean.Video
import com.hh.composeplayer.util.xmlToJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose.logic
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/16  15:31
 */
class HttpDataHelper {
    suspend fun getTabList() : List<Ty> = coroutineScope{
        val tabListr : MutableList<Ty> = ArrayList()
        val job = launch (IO) {
            val tabList = TaskApi.create(ApiService::class.java).getPlayer()
            val jsonObject = xmlToJson(tabList.toString())?.toJson()
            val homeTab = Gson().fromJson(jsonObject.toString(), Jsons::class.java)
            homeTab.apply {
                mJson = jsonObject.toString()
                saveOrUpdate("id = ?",id.toString())
                rss?.apply {
                    saveOrUpdate("id = ?",id.toString())
                    `class`?.apply {
                        saveOrUpdate("id = ?",id.toString())
                        ty?.apply {
                            tabListr.add(Ty("最新"))
                            tabListr.addAll(this)
                            forEachIndexed { index, ty ->
                                ty.saveOrUpdate("id = ?",(index+1).toString())
                            }
                        }
                    }
                }
            }
        }
        job.join()
        tabListr
    }

    suspend fun getPlayerList(state :Long) : List<Video> = coroutineScope {
        val movieList : MutableList<Video> = ArrayList()
        val job = launch (IO) {
            val movieStr = if(                                                                                    state == 0L){
                TaskApi.create(ApiService::class.java).getPlayerListZx(0)
            } else{
                TaskApi.create(ApiService::class.java).getPlayerList(state.toString(),1)
            }
            val jsonObject = xmlToJson(movieStr.toString())?.toJson()
            val movieBean = Gson().fromJson(jsonObject.toString(), MovieBean::class.java)
            movieBean.rss?.run {
                list?.apply {
                    video?.apply {
                        movieList.addAll(video)
                    }
                }
            }
        }
        job.join()
        movieList
    }
}