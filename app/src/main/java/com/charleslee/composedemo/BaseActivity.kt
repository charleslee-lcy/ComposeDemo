package com.charleslee.composedemo

import androidx.appcompat.app.AppCompatActivity
import com.charleslee.composedemo.speech.window.FloatingView


/**
 *
 * <p> Created by CharlesLee on 2024/3/25
 * 15708478830@163.com
 */
open class BaseActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        FloatingView.attach(this)
    }

    override fun onStop() {
        super.onStop()
        FloatingView.detach(this)
    }
}