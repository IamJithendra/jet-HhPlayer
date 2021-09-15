package com.hh.composeplayer.api.converter

import com.alibaba.fastjson.serializer.SerializeConfig
import retrofit2.Retrofit
import okhttp3.RequestBody
import okhttp3.ResponseBody
import kotlin.jvm.JvmOverloads
import retrofit2.Converter
import java.lang.NullPointerException
import java.lang.reflect.Type

/**
 * @ProjectName: ZngjMvvM
 * @Package: com.zngj.zngjmvvm.util.http.converter
 * @Description: 类描述
 * @Author: huanghai
 * @CreateDate: 2021/1/15  10:28
 */
class FastJsonConverterFactory private constructor(serializeConfig: SerializeConfig?) :
    Converter.Factory() {
    private val serializeConfig: SerializeConfig
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return FastJsonRequestBodyConverter<Any>(serializeConfig)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return FastJsonResponseBodyConvert<Any>(type)
    }

    companion object {
        @JvmOverloads
        fun create(serializeConfig: SerializeConfig? = SerializeConfig.getGlobalInstance()): FastJsonConverterFactory {
            return FastJsonConverterFactory(serializeConfig)
        }
    }

    init {
        if (serializeConfig == null) throw NullPointerException("serializeConfig == null")
        this.serializeConfig = serializeConfig
    }
}