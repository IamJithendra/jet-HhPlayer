package com.hh.composeplayer.util

import com.hh.composeplayer.logic.HttpDataHelper
import com.hh.composeplayer.logic.Repository

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.util
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/15  11:43
 */
object ServiceLocator {
    /**
     * Provide the Repository instance that ViewModel should depend on.
     */
    fun provideRepository() = Repository(provideDataHelper())

    /**
     * Provide the DataHelper instance that Repository should depend on.
     */
    private fun provideDataHelper() = HttpDataHelper()
}