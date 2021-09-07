package com.hh.composeplayer.api
import retrofit2.http.*

/**
 * 创建时间：2018/4/8
 * 编写人： hh
 * 功能描述：测试接口
 */
interface ApiService {
    //获取影视类型
    @GET("inc/api.php/")
    suspend fun getPlayer(): Any
    //获取影视类型列表
    @GET("inc/api.php/?ac=videolist")
    suspend fun getPlayerList(@Query("t") tid:String,@Query("pg") page:Int): Any
    //获取最新影视列表
    @GET("inc/api.php/?ac=videolist")
    suspend fun getPlayerListZx(@Query("pg") page:Int): Any
    //搜索影视列表
    @GET("inc/api.php/")
    suspend fun getSearchPlayerList(@Query("wd") wd:String,@Query("pg") page:Int): Any
    //查看影视详情
    @GET("inc/api.php/?ac=videolist")
    suspend fun getPlayerDetail(@Query("ids") ids:String): Any
}