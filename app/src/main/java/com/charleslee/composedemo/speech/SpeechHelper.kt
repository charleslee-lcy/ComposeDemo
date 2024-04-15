package com.charleslee.composedemo.speech

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import androidx.core.animation.addListener
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.baidu.tts.sample.MainHandlerConstant
import com.charleslee.composedemo.speech.SpeechHelper.DataKit.performResult
import com.charleslee.composedemo.speech.SpeechHelper.DataKit.waitUtilExit
import com.charleslee.composedemo.speech.SpeechHelper.UIKit.changeToListen
import com.charleslee.composedemo.speech.SpeechHelper.UIKit.changeToSpeak
import com.charleslee.composedemo.speech.SpeechHelper.UIKit.closeWindow
import com.charleslee.composedemo.speech.SpeechHelper.UIKit.showWindow
import com.charleslee.composedemo.speech.SpeechHelper.UIKit.updateContentText
import com.charleslee.composedemo.speech.SpeechHelper.UIKit.updateStatusText
import com.charleslee.composedemo.speech.utils.RxTimer
import com.charleslee.composedemo.speech.window.EnFloatingView
import com.charleslee.composedemo.speech.window.FloatingView
import org.json.JSONException
import org.json.JSONObject


/**
 * 语音交互相关
 * <p> Created by CharlesLee on 2024/3/29
 * 15708478830@163.com
 */
object SpeechHelper : EventListener {
    const val TAG = "SpeechHelper"
    const val STATUS_IDLE = "idle"
    const val STATUS_WAKEUP_SPEAK = "wakeup_speak"
    const val STATUS_SPEAK = "speak"
    const val STATUS_LISTEN = "listen"

    const val APP_ID = "38195273"
    const val APP_KEY = "38CD7v9dOqocYPtdaolqkpkD"
    const val SECRET_KEY = "MzPKm58vGZ6XbcUIyl5jRjGoem0TMb1u"

    private var status = STATUS_IDLE

    // 历史TTS文字记录
    private var ttsSpeechText = ""
    // 实时TTS文字记录
    private val tempSpeechText by lazy {
        StringBuilder("")
    }

    fun getDefaultParam(): MutableMap<String, Any> {
        return mutableMapOf(
            SpeechConstant.APP_ID to APP_ID,
            SpeechConstant.APP_KEY to APP_KEY,
            SpeechConstant.SECRET to SECRET_KEY
        )
    }

    private val ttsHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                MainHandlerConstant.PRINT -> Log.d(TAG, msg.obj as String)
                MainHandlerConstant.UI_CHANGE_TTS_PLAY_START -> {
                    ttsSpeechText = ""
                    tempSpeechText.clear()
                }
                MainHandlerConstant.UI_CHANGE_INPUT_TEXT_SELECTION -> {
                    tempSpeechText.clear()
                    tempSpeechText
                        .append(ttsSpeechText)
                        .append(TTSHelper.speakText.substring(0, msg.arg1))
                    updateContentText(tempSpeechText.toString())
                }
                MainHandlerConstant.UI_CHANGE_SYNTHES_TEXT_SELECTION -> {
                }
                MainHandlerConstant.UI_CHANGE_TTS_PLAY_NEXT -> {
                    TTSHelper.playContentList()
                    ttsSpeechText = tempSpeechText.toString()
                }
                MainHandlerConstant.UI_CHANGE_TTS_PLAY_END -> {
                    ttsSpeechText = ""
                    tempSpeechText.clear()

                    if (status == STATUS_SPEAK) {
                        waitUtilExit()
                    } else {
                        changeToListen()
                    }
                }
                else -> {}
            }
        }
    }

    init {
        // 语音唤醒监听
        WakeupHelper.registerListener(this)
        // 语音识别监听
        ASRHelper.registerListener(this)
    }

    /**
     * 语音唤醒开始监听
     */
    fun wakeupStart(context: Context) {
        WakeupHelper.start(context)
        initTTS()
    }

    /**
     * 语音唤醒停止监听
     */
    fun wakeupStop() {
        WakeupHelper.stop()
        status = STATUS_IDLE
    }

    fun ttsSpeech(content: String) {
        changeToSpeak()
        TTSHelper.speak(content)
    }

    override fun onEvent(
        name: String,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        Log.d(TAG, "event: name=${name}, params=${params}")

        when(name) {
            SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS -> { //唤醒事件
                try {
                    if (!params.isNullOrEmpty()) {
                        val result = JSONObject(params)
                        val errorCode = result.getInt("errorCode")
                        if (errorCode == 0) {
                            //唤醒成功
                            wakeupByVoice()
                        } else {
                            //唤醒失败
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            SpeechConstant.CALLBACK_EVENT_WAKEUP_STOPED -> { //唤醒停止
                FloatingView.remove()
            }
            else -> {
                var logTxt = "name: $name"
                if (name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL) {
                    // 识别相关的结果都在这里
                    if (params.isNullOrEmpty()) {
                        return
                    }
                    if (params.contains("\"nlu_result\"")) {
                        // 一句话的语义解析结果
                        data?.takeIf {
                            length > 0 && it.isNotEmpty()
                        }?.apply {
                            logTxt += ", 语义解析结果：" + String(this, offset, length)
                        }
                    } else if (params.contains("\"partial_result\"")) {
                        // 一句话的临时识别结果
                        logTxt += ", 临时识别结果：$params"

                        try {
                            val result = JSONObject(params).opt("best_result") as String
                            updateContentText(result)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else if (params.contains("\"final_result\"")) {
                        // 一句话的最终识别结果
                        logTxt += ", 最终识别结果：$params"

                        try {
                            val result = JSONObject(params).opt("best_result") as String
                            updateContentText(result)
                            performResult(result)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        // 一般这里不会运行
                        logTxt += " ;params :$params"
                        if (data != null) {
                            logTxt += " ;data length=" + data.size
                        }
                    }
                } else {
                    // 识别开始，结束，音量，音频数据回调
                    if (!params.isNullOrEmpty()) {
                        logTxt += " ;params :$params"
                    }
                    if (data != null) {
                        logTxt += " ;data length=" + data.size
                    }
                }
                Log.d(TAG, logTxt)
            }
        }
    }

    /**
     * 触发语音唤醒操作
     */
    private fun wakeupByVoice() {
        showWindow()
        TTSHelper.speak("您好，小封在呢")
        updateStatusText("我正在说")
        ASRHelper.stop()
        status = STATUS_WAKEUP_SPEAK
    }

    fun release() {
        releaseWakeup()
        releaseASR()
        releaseTTS()
    }

    /**
     * 初始化文本转语音
     */
    private fun initTTS() {
        TTSHelper.initialTts(ttsHandler)
    }

    private fun releaseTTS() {
        TTSHelper.stop()
        TTSHelper.release()
    }

    private fun releaseWakeup() {
        WakeupHelper.stop()
        WakeupHelper.release()
    }

    private fun releaseASR() {
        ASRHelper.stop()
        ASRHelper.release()
    }

    /**
     * UI更新相关
     */
    private object UIKit {
        /**
         * 打开浮窗
         */
        fun showWindow() {
            if (FloatingView.view == null) {
                FloatingView.add()
                val view = FloatingView.view as EnFloatingView
                showAnim(view)

                view.binding.ivClose.setOnClickListener {
                    closeWindow()
                }
                view.binding.ivSetting.setOnClickListener {

                }
                view.binding.ivReset.setOnClickListener {
                    RxTimer.cancelTimer()
                    TTSHelper.stop()
                    changeToHome()
                }
            }
        }

        /**
         * 关闭浮窗
         */
        fun closeWindow() {
            FloatingView.view?.apply {
                hideAnim(this)
            }
            ASRHelper.stop()
            TTSHelper.stop()
        }

        /**
         * 更新状态文案
         */
        fun updateStatusText(text: String) {
            FloatingView.view?.takeIf {
                it is EnFloatingView
            }?.apply {
                val view = this as EnFloatingView
                view.binding.tvStatus.text = text
            }
        }

        /**
         * 更新内容UI
         */
        fun updateContentText(text: String) {
            if (status == STATUS_WAKEUP_SPEAK) return

            FloatingView.view?.takeIf {
                it is EnFloatingView
            }?.apply {
                val view = this as EnFloatingView
                view.binding.clHint.visibility = View.INVISIBLE
                view.binding.tvResult.visibility = View.VISIBLE
                view.binding.tvResult.text = text
            }
        }

        fun showHomeUI() {
            FloatingView.view?.takeIf {
                it is EnFloatingView
            }?.apply {
                val view = this as EnFloatingView
                view.binding.clHint.visibility = View.VISIBLE
                view.binding.tvResult.visibility = View.INVISIBLE
            }
        }

        fun changeToSpeak() {
            updateStatusText("我正在说")
            ASRHelper.stop()
            status = STATUS_SPEAK
        }

        fun changeToListen() {
            updateStatusText("我正在听")
            status = STATUS_LISTEN
            ASRHelper.start()
        }

        fun changeToHome() {
            updateStatusText("我正在听")
            showHomeUI()
            status = STATUS_LISTEN
            ASRHelper.start()
        }

        fun showAnim(view: View) {
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
            val alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
//            val translationY = ObjectAnimator.ofFloat(view, "translationY", 300f, 0f)
            val set = AnimatorSet()
            set.duration = 300
            set.playTogether(scaleX, scaleY, alpha)
            set.start()
        }

        fun hideAnim(view: View) {
            val animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
            animator.duration = 300
            animator.addListener(object: AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    FloatingView.remove()
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }

            })
            animator.start()
        }
    }

    /**
     * 接口数据交互相关
     */
    private object DataKit {
        /**
         * 请求语义判断
         */
        fun performResult(result: String) {
            if (result.contains("天气")) {
                ttsSpeech("今日成都天气，多云转晴，气温111至24度，体感温度18度，空气质量优。今日成都天气，多云转晴，气温11至14度，体感温度适。今日气温较昨日有所回升，预计最高温度将达到18度，最低温度也有10度。虽然气温回升，但天气仍然较冷，请市民们注意保暖。")
            }
        }

        /**
         * 处理对话完成等待三秒的场景
         */
        fun waitUtilExit() {
            RxTimer.timer(3000) {
                closeWindow()
            }
        }
    }
}