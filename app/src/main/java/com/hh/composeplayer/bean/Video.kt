package com.hh.composeplayer.bean

data class Video(
    val dt: String,
    val id: String,
    val last: String,
    val name: String,
    val note: String,
    val tid: String,
    val type: String,
    val area: String? = null,
    val year: String? = null,
    val director: String? = null,
    val dl: PlayerSiteList? = null,
    val pic: String? = null,
    val actor: String? = null,
    val des: String? = null,
    val state: String? = null,
    val lang: String? = null,
)

data class PlayerSiteList(
    val dd : MutableList<PlayerSite>
)

data class PlayerSite(
    val content : String,val flag : String,val contentList:List<String>
)