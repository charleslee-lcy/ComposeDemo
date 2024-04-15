package com.charleslee.composedemo.speech

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.baidu.aip.asrwakeup3.core.mini.AutoCheck
import com.baidu.speech.EventListener
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.charleslee.composedemo.App
import org.json.JSONObject


/**
 * 语音识别相关
 * <p> Created by CharlesLee on 2024/3/29
 * 15708478830@163.com
 */
object ASRHelper {
    private const val TAG = "ASRHelper"

    private var listener: EventListener? = null
    private var enableOffline = false // 测试离线命令词，需要改成true


    private val asr by lazy {
        EventManagerFactory.create(App.application, "asr")
    }

    init {
        if (enableOffline) {
            loadOfflineEngine() // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
    }

    fun registerListener(listener: EventListener) {
        this.listener = listener
        // 基于sdk集成1.3 注册自己的输出事件类
        this.listener?.apply {
            asr.registerListener(this)
        }
    }

    /**
     * 基于SDK集成2.2 发送开始事件
     * 点击开始按钮
     * 测试参数填在这里
     */
    fun start() {
        val params = SpeechHelper.getDefaultParam()
        val event = SpeechConstant.ASR_START // 替换成测试的event


        if (enableOffline) {
            params[SpeechConstant.DECODER] = 2
        }
        // 基于SDK集成2.1 设置识别参数
        // 基于SDK集成2.1 设置识别参数
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.BDS_ASR_ENABLE_LONG_SPEECH, true);//长语音  优先级高于VAD_ENDPOINT_TIMEOUT
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音

        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号

        /* 语音自训练平台特有参数 */
        // params.put(SpeechConstant.PID, 8002);
        // 语音自训练平台特殊pid，8002：模型类似开放平台 1537  具体是8001还是8002，看自训练平台页面上的显示
        // params.put(SpeechConstant.LMID,1068);
        // 语音自训练平台已上线的模型ID，https://ai.baidu.com/smartasr/model
        // 注意模型ID必须在你的appId所在的百度账号下
        /* 语音自训练平台特有参数 */

        /* 测试InputStream*/
        // InFileStream.setContext(this);
        // params.put(SpeechConstant.IN_FILE,
        // "#com.baidu.aip.asrwakeup3.core.inputstream.InFileStream.createMyPipedInputStream()");

        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.BDS_ASR_ENABLE_LONG_SPEECH, true);//长语音  优先级高于VAD_ENDPOINT_TIMEOUT
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音

        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号

        /* 语音自训练平台特有参数 */
        // params.put(SpeechConstant.PID, 8002);
        // 语音自训练平台特殊pid，8002：模型类似开放平台 1537  具体是8001还是8002，看自训练平台页面上的显示
        // params.put(SpeechConstant.LMID,1068);
        // 语音自训练平台已上线的模型ID，https://ai.baidu.com/smartasr/model
        // 注意模型ID必须在你的appId所在的百度账号下
        /* 语音自训练平台特有参数 */

        /* 测试InputStream*/
        // InFileStream.setContext(this);
        // params.put(SpeechConstant.IN_FILE,
        // "#com.baidu.aip.asrwakeup3.core.inputstream.InFileStream.createMyPipedInputStream()");

        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        AutoCheck(App.application, object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                if (msg.what == 100) {
                    val autoCheck = msg.obj as AutoCheck
                    synchronized(autoCheck) {
                        val message = autoCheck.obtainErrorMessage()
                        Log.d(TAG, message)
                    }
                }
            }
        }, enableOffline).checkAsr(params)
        val json = JSONObject(params as Map<*, *>).toString() // 这里可以替换成你需要测试的json

        asr.send(event, json, null, 0, 0)
        Log.d(TAG, "输入参数：$json")
    }

    /**
     * 点击停止按钮
     * 基于SDK集成4.1 发送停止事件
     */
    fun stop() {
        Log.d(TAG, "停止识别：ASR_STOP")
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0)
    }

    fun release() {
        // 基于SDK集成4.2 发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
        if (enableOffline) {
            unloadOfflineEngine() // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        this.listener?.apply {
            asr.unregisterListener(this)
        }
    }


    /**
     * enableOffline设为true时，在onCreate中调用
     * 基于SDK离线命令词1.4 加载离线资源(离线时使用)
     */
    private fun loadOfflineEngine() {
        val params = mutableMapOf<String, Any>()
        params[SpeechConstant.DECODER] = 2
        params[SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH] =
            "assets://baidu_speech_grammar.bsg"
        asr.send(
            SpeechConstant.ASR_KWS_LOAD_ENGINE,
            JSONObject(params as Map<*, *>).toString(),
            null,
            0,
            0
        )
    }

    /**
     * enableOffline为true时，在onDestory中调用，与loadOfflineEngine对应
     * 基于SDK集成5.1 卸载离线资源步骤(离线时使用)
     */
    private fun unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0) //
    }

}