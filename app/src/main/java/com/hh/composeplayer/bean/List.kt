package com.hh.composeplayer.bean

data class ListVideo(
    val page: String,
    val pagecount: String,
    val pagesize: String,
    val recordcount: String,
    val video: List<Video>?
)