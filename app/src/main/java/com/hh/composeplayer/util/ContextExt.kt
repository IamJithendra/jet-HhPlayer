package com.hh.composeplayer.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.hh.composeplayer.R
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


fun Context.stringResource(@StringRes rid:Int) : String{
    return resources.getString(rid)
}

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 *
 */
suspend fun Context.showMessage(
    message: String,
    title: String = stringResource(R.string.tips),
    positiveButtonText: String = stringResource(R.string.confirm),
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {

    MaterialDialog(this)
        .cancelable(true)
        .show {
            title(text = title)
            message(text = message)
            positiveButton(text = positiveButtonText) {
                positiveAction.invoke()
            }
            if (negativeButtonText.isNotEmpty()) {
                negativeButton(text = negativeButtonText) {
                    negativeAction.invoke()
                }
            }
            getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor())
            getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor())
        }
}



