package com.hh.composeplayer.api

import com.hh.composeplayer.BuildConfig


/**
 * @ProjectName: HHplayer
 * @Package: com.hh.hhplayer.base
 * @Description: 类描述
 * @Author: huanghai
 * @CreateDate: 2021/5/12  15:03
 */
object HttpUrl {
    private val ISRELESE: Boolean = BuildConfig.DEBUG
    val baseHostUrl: String = "https://www.88zy.live/"
}