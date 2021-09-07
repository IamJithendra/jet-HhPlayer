package com.hh.composeplayer.api

import com.hh.composeplayer.HhCpApp
import com.hh.composeplayer.api.gsonfactory.GsonConverterFactory.Companion.create
import retrofit2.Retrofit

/**
 * @ProjectName: ZngjMvvM
 * @Package: com.zngj.zngjmvvm.api
 * @Description: 类描述
 * @Author: huanghai
 * @CreateDate: 2021/1/15  12:00
 */
object TaskApi {

    @JvmStatic
    fun <T> create(service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(HttpUrl.baseHostUrl)
            .client(HhCpApp.instance.getOkBuilder())
            .addConverterFactory(create())
            .build()
        return retrofit.create(service)
    }
    @JvmStatic
    fun <T> create(service: Class<T>, url: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(HhCpApp.instance.getOkBuilder())
            .addConverterFactory(create())
            .build()
        return retrofit.create(service)
    }
}