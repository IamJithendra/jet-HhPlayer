package com.hh.composeplayer.api.gsonfactory

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody;
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer;
import retrofit2.Converter;

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.api.gsonfactory
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/30  15:12
 */
internal class GsonRequestBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) :
    Converter<T, RequestBody> {
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter: JsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
    companion object {
        private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
        private val UTF_8: Charset = Charset.forName("UTF-8")
    }
}
