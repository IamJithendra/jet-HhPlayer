package com.hh.composeplayer.bean

import org.litepal.crud.LitePalSupport

/**
 * @ProjectName: HHplayer
 * @Package: com.hh.hhplayer.bean
 * @Description: 类描述
 * @Author: huanghai
 * @CreateDate: 2021/6/3  11:35
 */
class CollectBusK : LitePalSupport(){
    var collectId : Int? = null
    var flag : Boolean = false
    var cName : String? = null
    var type : String? = null
}
