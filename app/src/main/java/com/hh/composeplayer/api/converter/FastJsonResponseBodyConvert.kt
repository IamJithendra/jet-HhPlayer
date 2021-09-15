package com.hh.composeplayer.api.converter

import okhttp3.ResponseBody
import kotlin.Throws
import com.alibaba.fastjson.JSON
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

/**
 * @ProjectName: ZngjMvvM
 * @Package: com.zngj.zngjmvvm.util.http.converter
 * @Description: 类描述
 * @Author: huanghai
 * @CreateDate: 2021/1/15  10:29
 */
internal class FastJsonResponseBodyConvert<T>(private val type: Type) : Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val s = value.string()
        return if ("class java.lang.Object" == type.toString()) {
            s as T
        } else JSON.parseObject(s, type)
    }
}