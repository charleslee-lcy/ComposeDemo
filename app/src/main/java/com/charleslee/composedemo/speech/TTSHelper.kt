package com.charleslee.composedemo.speech

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.util.Pair
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import com.baidu.tts.sample.control.InitConfig
import com.baidu.tts.sample.control.MySyntherizer
import com.baidu.tts.sample.control.NonBlockSyntherizer
import com.baidu.tts.sample.listener.UiMessageListener
import com.baidu.tts.sample.util.AutoCheck
import com.charleslee.composedemo.App


/**
 * 语音合成相关
 * <p> Created by CharlesLee on 2024/3/29
 * 15708478830@163.com
 */
object TTSHelper {
    private var playIndex = 0
    private const val TAG = "TTSHelper"
    private const val CONTENT_SPLIT_LENGTH = 59

    var speakText = ""
    private var mContentList = mutableListOf<String>()

    // 主控制类，所有合成控制方法从这个类开始
    @SuppressLint("StaticFieldLeak")
    private var synthesizer: MySyntherizer? = null

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private val ttsMode: TtsMode = TtsMode.ONLINE

    private var listener: UiMessageListener? = null

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return 合成参数Map
     */
    private fun getTTSParams(): Map<String, String> {
        // 以下参数均为选填
        return mutableMapOf(
            // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>, 其它发音人见文档
            SpeechSynthesizer.PARAM_SPEAKER to "0",
            // 设置合成的音量，0-15 ，默认 5
            SpeechSynthesizer.PARAM_VOLUME to "15",
            // 设置合成的语速，0-15 ，默认 5
            SpeechSynthesizer.PARAM_SPEED to "7",
            // 设置合成的语调，0-15 ，默认 5
            SpeechSynthesizer.PARAM_PITCH to "5"
        )
    }

    private fun getInitConfig(listener: SpeechSynthesizerListener?): InitConfig {
        val params: Map<String, String> = getTTSParams()
        // 添加你自己的参数
        val initConfig = InitConfig(
            SpeechHelper.APP_ID,
            SpeechHelper.APP_KEY,
            SpeechHelper.SECRET_KEY,
            ttsMode,
            params,
            listener
        )
        // 上线时请删除AutoCheck的调用
        AutoCheck.getInstance(App.application)
            .check(initConfig, object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    if (msg.what == 100) {
                        val autoCheck = msg.obj as AutoCheck
                        synchronized(autoCheck) {
                            val message = autoCheck.obtainDebugMessage()
                            Log.d(TAG, message)
                        }
                    }
                }
            })
        return initConfig
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     *
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    fun initialTts(ttsHandler: Handler) {
        LoggerProxy.printable(true) // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        listener = UiMessageListener(ttsHandler)
        val config = getInitConfig(listener)
        synthesizer = NonBlockSyntherizer(App.application, config, ttsHandler) // 此处可以改为MySyntherizer 了解调用过程
    }

    /**
     * 合成语音并播放
     */
    fun speak(text: String?) {
        // 需要合成的文本text的长度不能超过1024个GBK字节。
        if (text.isNullOrEmpty()) return
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // params.put(SpeechSynthesizer.PARAM_SPEAKER, "3"); // 设置为度逍遥
        // synthesizer.setParams(params);

        splitContent(text)

        playIndex = 0

        listener?.apply {
            speechStart()
        }

        playContentList()
    }

    fun playContentList() {
        if (playIndex >= mContentList.size) {
            Log.d(TAG, "playContentList mContentList is empty")
            listener?.apply {
                speechEnd()
            }
            return
        }

        val curText = mContentList[playIndex]

        Log.d(TAG, "playContentList content:$curText")
        val result: Int = synthesizer?.speak(curText) ?: -1
        this.speakText = curText
        checkResult(result, "speak")
        playIndex++
    }

    /**
     * 批量播放
     */
    fun batchSpeak(texts: MutableList<Pair<String, String>>) {
//        texts.add(Pair("开始批量播放，", "a0"))
//        texts.add(Pair("123456，", "a1"))
//        texts.add(Pair("欢迎使用百度语音，，，", "a2"))
//        texts.add(Pair("重(chong2)量这个是多音字示例", "a3"))
        val result: Int = synthesizer?.batchSpeak(texts) ?: -1
        checkResult(result, "batchSpeak")
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    fun pause() {
        val result: Int = synthesizer?.pause() ?: -1
        checkResult(result, "pause")
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    fun resume() {
        val result: Int = synthesizer?.resume() ?: -1
        checkResult(result, "resume")
    }

    /**
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    fun stop() {
        playIndex = 0
        val result: Int = synthesizer?.stop() ?: -1
        checkResult(result, "stop")
    }

    fun release() {
        synthesizer?.release()
        Log.d(TAG, "TTSHelper.release")
    }

    private fun checkResult(result: Int, method: String) {
        if (result != 0) {
            Log.d(TAG, "error code :$result method:$method")
        }
    }

    private fun splitContent(text: String) {
        var content = text
        if (TextUtils.isEmpty(content)) {
            return
        }
        mContentList.clear()
        content = if (TextUtils.isEmpty(content)) {
            return
        } else {
            content.replace(" ", "").replace("&nbsp;", "")
        }
        while (content.length > 50) {
            var index = content.indexOf("。")
            if (index <= 0 || index >= CONTENT_SPLIT_LENGTH) {
                // 未找到句号，强制从指定位置截断
                index = CONTENT_SPLIT_LENGTH
            }
            content = try {
                mContentList.add(content.substring(0, index + 1))
                content.substring(index + 1)
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }
        if (!TextUtils.isEmpty(content)) {
            mContentList.add(content)
        }
    }
}