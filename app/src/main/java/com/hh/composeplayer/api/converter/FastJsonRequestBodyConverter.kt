package com.hh.composeplayer.api.converter

import com.alibaba.fastjson.serializer.SerializeConfig
import okhttp3.RequestBody
import kotlin.Throws
import com.alibaba.fastjson.JSON
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import java.io.IOException

/**
 * @ProjectName: HHPlayer
 * @Package: com.hh.composeplayer.api.converter
 * @Description: 类描述
 * @Author: huanghai
 * @CreateDate: 2021/1/15  10:28
 */
internal class FastJsonRequestBodyConverter<T>(private val serializeConfig: SerializeConfig) :
    Converter<T, RequestBody> {
    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value, serializeConfig))
    }

    companion object {
        private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
    }

}