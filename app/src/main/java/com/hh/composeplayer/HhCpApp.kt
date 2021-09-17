package com.hh.composeplayer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Environment
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.hellocompose
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/17  8:43
 */
class HhCpApp : Application(){
    lateinit var okbuilder : OkHttpClient
    companion object {
        /**
         * application context.
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: HhCpApp
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instance = this
        initRetrofit()
    }

    private fun initRetrofit(): OkHttpClient {
        //请求头
        val headerInterceptor = Interceptor { chain: Interceptor.Chain ->
            val orignaRequest = chain.request()
            val request = orignaRequest.newBuilder()
//                .header("Authorization", "Bearer $token")
                .method(orignaRequest.method, orignaRequest.body)
                .build()
            chain.proceed(request)
        }
        val cacheFile =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "http_cache")
        val cache = Cache(cacheFile, 104857600L) // 指定缓存大小100Mb
        val builder = OkHttpClient.Builder()
        //        builder.addInterceptor(addQueryParameterInterceptor);
        builder.addInterceptor(headerInterceptor)
        builder.cache(cache)
        builder.readTimeout(60000, TimeUnit.MILLISECONDS)
        //全局的写入超时时间60s
        builder.writeTimeout(60000, TimeUnit.MILLISECONDS)
        //全局的连接超时时间30s
        builder.connectTimeout(30000, TimeUnit.MILLISECONDS)
        okbuilder = builder.build()
        return builder.build()
    }

    fun getOkBuilder(): OkHttpClient {
        return okbuilder
    }
}