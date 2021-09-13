package com.hh.composeplayer.manager

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hh.composeplayer.R
import com.hh.composeplayer.logic.HttpDataHelper
import com.hh.composeplayer.logic.Repository
import com.hh.composeplayer.util.BitmapBlur
import com.hh.composeplayer.util.HhUtil
import com.hh.composeplayer.util.Mylog.e
import java.util.*

/**
 * @ProjectName: HelloComPose
 * @Package: com.hh.composeplayer.manager
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/9/13  8:42
 * 处理不同API级别的兼容性问题
 * 考虑设备运行状况以执行任务
 * 我们可以添加网络约束
 * 我们可以安排单次任务，定期任务，任务链（并行和顺序）
 * 灵活的重试政策
 * 保证工作执行
 */
class TabListWorkManager(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val repository = Repository(HttpDataHelper())
        val list = repository.getTabList()
        e("HHLog","TabListWorkManager${HhUtil.formatDate(Date(System.currentTimeMillis()),"yyyy-MM-dd HH:mm:ss")}" )
        return if(list.isNotEmpty()){
            Result.success()
        } else{
            Result.failure()
        }
    }
}