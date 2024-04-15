package com.charleslee.composedemo

import android.app.Application


/**
 *
 * <p> Created by CharlesLee on 2024/3/29
 * 15708478830@163.com
 */
class App : Application() {
    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}