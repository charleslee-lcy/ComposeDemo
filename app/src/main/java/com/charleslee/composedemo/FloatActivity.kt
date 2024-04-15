package com.charleslee.composedemo

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.baidu.aip.asrwakeup3.core.mini.ActivityMiniWakeUp
import com.baidu.tts.sample.SynthActivity
import com.charleslee.composedemo.databinding.ActivityFloatBinding
import com.charleslee.composedemo.speech.SpeechHelper
import com.charleslee.composedemo.speech.TTSHelper
import com.charleslee.composedemo.speech.WakeupHelper
import com.charleslee.composedemo.speech.window.FloatingMagnetView
import com.charleslee.composedemo.speech.window.FloatingView
import com.charleslee.composedemo.speech.window.MagnetViewListener
import com.charleslee.composedemo.speech.utils.SystemUtils


/**
 *
 * <p> Created by CharlesLee on 2024/3/25
 * 15708478830@163.com
 */
class FloatActivity : BaseActivity() {
    private lateinit var binding: ActivityFloatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFloatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 悬浮窗事件监听
        FloatingView.listener(object: MagnetViewListener {
            override fun onClick(magnetView: FloatingMagnetView?) {
            }

            override fun onRemove(magnetView: FloatingMagnetView?) {
            }
        })

        val screenWidth = SystemUtils.getScreenWidth(this)
        val params = FrameLayout.LayoutParams(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        params.setMargins(0, 0, 0, 500)
        FloatingView.layoutParams(params)

        SpeechHelper.wakeupStart(this)

        binding.show.setOnClickListener {
            TTSHelper.speak("您好，小封在呢")
        }

        binding.hide.setOnClickListener {
        }

        binding.newPage.setOnClickListener {
        }
    }

    override fun onDestroy() {
        SpeechHelper.release()
        super.onDestroy()
    }
}