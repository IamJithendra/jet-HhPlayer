package com.hh.composeplayer.api.gsonfactory

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter


/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.api.gsonfactory
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/30  15:03
 */
internal class GsonResponseBodyConverter<T>(private val gson: Gson,private val adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T{
        val s = value.string()
        return s as T
    }

}