package com.charleslee.composedemo.speech.window

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import com.charleslee.composedemo.R
import com.charleslee.composedemo.speech.utils.EnContext
import java.lang.ref.WeakReference

object FloatingView : IFloatingView {
    override var view: FloatingMagnetView? = null

    private var mContainer: WeakReference<FrameLayout?>? = null

    @LayoutRes
    private var mLayoutId = R.layout.en_floating_view

    @DrawableRes
    private var mIconRes = R.mipmap.speech_float_avatar

    private var mLayoutParams: ViewGroup.LayoutParams? = params

    private val container: FrameLayout?
        get() = if (mContainer == null) null else mContainer?.get()

    private val params: FrameLayout.LayoutParams
        get() {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.setMargins(13, params.topMargin, params.rightMargin, 500)
            return params
        }

    private fun ensureFloatingView() {
        synchronized(this) {
            if (view != null) {
                return
            }
            val enFloatingView = EnFloatingView(EnContext.get(), mLayoutId)
            enFloatingView.setLayoutParams(mLayoutParams)
            enFloatingView.setIconImage(mIconRes)
            view = enFloatingView
            addViewToWindow(enFloatingView)
        }
    }

    override fun attach(activity: Activity?): FloatingView {
        attach(getActivityRoot(activity))
        return this
    }

    override fun attach(container: FrameLayout?): FloatingView {
        if (container == null || view == null) {
            mContainer = WeakReference(container)
            return this
        }
        if (view?.parent === container) {
            return this
        }
        if (view?.parent != null) {
            (view?.parent as ViewGroup).removeView(view)
        }
        mContainer = WeakReference(container)
        container.addView(view)
        return this
    }

    override fun detach(activity: Activity?): FloatingView {
        detach(getActivityRoot(activity))
        return this
    }

    override fun detach(container: FrameLayout?): FloatingView {
        if (view != null && container != null && ViewCompat.isAttachedToWindow(view!!)) {
            container.removeView(view)
        }
        if (FloatingView.container === container) {
            mContainer = null
        }
        return this
    }

    override fun add(): FloatingView {
        ensureFloatingView()
        return this
    }

    override fun remove(): FloatingView {
        Handler(Looper.getMainLooper()).post {
            view?.let {
                if (ViewCompat.isAttachedToWindow(it) && container != null) {
                    container?.removeView(view)
                }
                view = null
            }
        }
        return this
    }

    override fun icon(@DrawableRes resId: Int): FloatingView {
        mIconRes = resId
        return this
    }

    override fun customView(viewGroup: FloatingMagnetView?): FloatingView {
        view = viewGroup
        return this
    }

    override fun customView(@LayoutRes resource: Int): FloatingView {
        mLayoutId = resource
        return this
    }

    override fun layoutParams(params: ViewGroup.LayoutParams?): FloatingView {
        mLayoutParams = params
        view?.setLayoutParams(params)
        return this
    }

    override fun listener(magnetViewListener: MagnetViewListener?): FloatingView {
        view?.setMagnetViewListener(magnetViewListener)
        return this
    }

    private fun addViewToWindow(view: View) {
        container?.addView(view)
    }

    private fun getActivityRoot(activity: Activity?): FrameLayout? {
        if (activity == null) {
            return null
        }
        try {
            return activity.window.decorView.findViewById<View>(android.R.id.content) as FrameLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}