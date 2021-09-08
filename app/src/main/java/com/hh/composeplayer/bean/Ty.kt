package com.hh.composeplayer.bean

import com.google.gson.annotations.SerializedName
import org.litepal.crud.LitePalSupport


class Ty() : LitePalSupport(){
    var content: String? = null
    @SerializedName("id")
    var staffId: Long = 0

    var pageId:Int = 0

    constructor(content : String) : this() {
        this.content = content
        staffId = 0
    }
}