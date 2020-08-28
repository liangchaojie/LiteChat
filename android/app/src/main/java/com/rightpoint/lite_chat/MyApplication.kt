package com.rightpoint.lite_chat

import android.util.Log
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.tencent.bugly.crashreport.CrashReport
import io.flutter.app.FlutterApplication

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/7/9 10:50 AM
 */
class MyApplication : FlutterApplication() {
    private val TAG = "MyApplication"
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        CrashReport.initCrashReport(applicationContext, "b7dc554861", false)
        val options = EMOptions()
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.acceptInvitationAlways = false
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.autoTransferMessageAttachments = true
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true)
        // 初始化
        EMClient.getInstance().init(applicationContext, options)
        // 在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true)
    }
}