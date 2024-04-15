package com.charleslee.composedemo.speech

import android.content.Context
import android.util.Log
import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream
import com.baidu.speech.EventListener
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.charleslee.composedemo.App
import com.charleslee.composedemo.speech.window.FloatingView
import org.json.JSONException
import org.json.JSONObject


/**
 * 语音唤醒相关
 * <p> Created by CharlesLee on 2024/3/29
 * 15708478830@163.com
 */
object WakeupHelper {
    private const val TAG = "WakeupHelper"
    private var listener: EventListener? = null

    private val wakeup by lazy {
        EventManagerFactory.create(App.application, "wp")
    }

    fun registerListener(listener: EventListener) {
        this.listener = listener
    }

    /**
     * 测试参数填在这里
     * 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
     */
    fun start(context: Context) {
        // 基于SDK唤醒词集成1.3 注册输出事件
        listener?.apply {
            wakeup.registerListener(this)
        }

        // 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
        val params = SpeechHelper.getDefaultParam()
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        params[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        InFileStream.setContext(context)
        val json = JSONObject(params as Map<*, *>).toString()
        wakeup.send(SpeechConstant.WAKEUP_START, json, null, 0, 0)
        Log.d(TAG, "输入参数：$json")
    }

    // 基于SDK唤醒词集成第4.1 发送停止事件
    fun stop() {
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0) //
    }

    fun release() {
        // 基于SDK集成4.2 发送取消事件
        wakeup.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)

        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        listener?.apply {
            wakeup.unregisterListener(this)
        }
    }
}
