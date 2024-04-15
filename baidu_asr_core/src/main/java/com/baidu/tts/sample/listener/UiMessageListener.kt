package com.baidu.tts.sample.listener

import android.os.Handler
import android.os.Message
import android.util.Log

/**
 * 在 MessageListener的基础上，和UI配合。
 * Created by fujiayi on 2017/9/14.
 */
open class UiMessageListener(private val mainHandler: Handler?) : MessageListener() {
    companion object {
        private const val TAG = "UiMessageListener"
    }

    override fun onSpeechStart(utteranceId: String) {
        super.onSpeechStart(utteranceId)
    }

    /**
     * 语音流 16K采样率 16bits编码 单声道 。
     *
     *
     * 合成数据和进度的回调接口，分多次回调。
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId
     * @param data        合成的音频数据。该音频数据是采样率为16K，2字节精度，单声道的pcm数据。
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     * engineType  下版本提供 1:音频数据由离线引擎合成； 0：音频数据由在线引擎（百度服务器）合成。
     */
    override fun onSynthesizeDataArrived(utteranceId: String, data: ByteArray, progress: Int) {
        super.onSynthesizeDataArrived(utteranceId, data, progress)
        mainHandler?.sendMessage(mainHandler.obtainMessage(UI_CHANGE_SYNTHES_TEXT_SELECTION, progress, 0))
    }

    /**
     * 播放进度回调接口，分多次回调
     * 注意：progress表示进度，与播放到哪个字无关
     *
     * @param utteranceId
     * @param progress    文本按字符划分的进度，比如:你好啊 进度是0-3
     */
    override fun onSpeechProgressChanged(utteranceId: String, progress: Int) {
        mainHandler?.sendMessage(mainHandler.obtainMessage(UI_CHANGE_INPUT_TEXT_SELECTION, progress, 0))
    }

    override fun onSpeechFinish(utteranceId: String) {
        super.onSpeechFinish(utteranceId)
        mainHandler?.sendMessage(mainHandler.obtainMessage(UI_CHANGE_TTS_PLAY_NEXT))
    }

    fun speechStart() {
        mainHandler?.sendMessage(mainHandler.obtainMessage(UI_CHANGE_TTS_PLAY_START))
    }

    fun speechEnd() {
        mainHandler?.sendMessage(mainHandler.obtainMessage(UI_CHANGE_TTS_PLAY_END))
    }

    protected fun sendMessage(message: String) {
        sendMessage(message, false)
    }

    override fun sendMessage(message: String, isError: Boolean) {
        sendMessage(message, isError, PRINT)
    }

    protected fun sendMessage(message: String, isError: Boolean, action: Int) {
        super.sendMessage(message, isError)
        mainHandler?.apply {
            Log.i(TAG, message)

            val msg = Message.obtain()
            msg.what = action
            msg.obj = message + "\n"
            sendMessage(msg)
        }
    }
}
