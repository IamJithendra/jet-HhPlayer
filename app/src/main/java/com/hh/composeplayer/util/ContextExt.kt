package com.hh.composeplayer.util

import android.content.Context
import android.widget.Toast
import fr.arnaudguyon.xmltojsonlib.XmlToJson

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/31  11:58
 */

fun Context.showToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun xmlToJson(xmlString: String?): XmlToJson? {
    try {
        return XmlToJson.Builder(xmlString!!).build()
    } catch (e: Exception) {
    }
    return null
}