package com.hh.composeplayer.bean

import org.litepal.crud.LitePalSupport

class Rss : LitePalSupport(){
    var id : Int ? = 1
    var `class`: Class? = null
    var list: ListVideo? = null
    var version: String? = null
}