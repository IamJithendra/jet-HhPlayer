package com.hh.composeplayer.bean

class Video{
    var dt: String? = null
    var id: String? = null
    var last: String? = null
    var name: String? = null
    var note: String? = null
    var tid: String? = null
    var type: String? = null
    var area: String? = null
    var year: String? = null
    var director: String? = null
    var dl: PlayerSiteList? = null
    var pic: String? = null
    var actor: String? = null
    var des: String? = null
    var state: String? = null
    var lang: String? = null
    var pageCount : String? =null
}

class PlayerSiteList{
    var dd : MutableList<PlayerSite>? = null
}

class PlayerSite{
    var content : String? = null
    var flag : String? = null
    var contentList:List<String>? = null
}