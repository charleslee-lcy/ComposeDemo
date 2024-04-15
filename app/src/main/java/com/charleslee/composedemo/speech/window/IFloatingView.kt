package com.charleslee.composedemo.speech.window

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

interface IFloatingView {

    fun attach(activity: Activity?): FloatingView?

    fun attach(container: FrameLayout?): FloatingView?

    fun detach(activity: Activity?): FloatingView?

    fun detach(container: FrameLayout?): FloatingView?

    val view: FloatingMagnetView?

    fun add(): FloatingView?

    fun remove(): FloatingView?

    fun icon(@DrawableRes resId: Int): FloatingView?

    fun customView(viewGroup: FloatingMagnetView?): FloatingView?

    fun customView(@LayoutRes resource: Int): FloatingView?

    fun layoutParams(params: ViewGroup.LayoutParams?): FloatingView?

    fun listener(magnetViewListener: MagnetViewListener?): FloatingView?

}
