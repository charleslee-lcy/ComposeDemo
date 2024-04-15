package com.charleslee.composedemo.speech.window

import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import com.charleslee.composedemo.speech.utils.SystemUtils
import kotlin.math.max
import kotlin.math.min

open class FloatingMagnetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mOriginalRawX = 0f
    private var mOriginalRawY = 0f
    private var mOriginalX = 0f
    private var mOriginalY = 0f
    private var mMagnetViewListener: MagnetViewListener? = null
    private var mLastTouchDownTime: Long = 0
    private var mMoveAnimator = MoveAnimator()
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mStatusBarHeight = 0
    private var mPortraitY = 0f

    companion object {
        const val MARGIN_EDGE = 13
        private const val TOUCH_TIME_THRESHOLD = 150
    }

    fun setMagnetViewListener(magnetViewListener: MagnetViewListener?) {
        mMagnetViewListener = magnetViewListener
    }

    init {
        mStatusBarHeight = SystemUtils.getStatusBarHeight(
            context
        )
        isClickable = true
//        updateSize();
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                changeOriginalTouchParams(event)
                updateSize()
                mMoveAnimator.stop()
            }

            MotionEvent.ACTION_MOVE -> updateViewPosition(event)
            MotionEvent.ACTION_UP -> {
                clearPortraitY()
                moveToEdge()
                if (isOnClickEvent) {
                    dealClickEvent()
                }
            }
        }
        return true
    }

    private fun dealClickEvent() {
        mMagnetViewListener?.onClick(this)
    }

    private val isOnClickEvent: Boolean
        get() = System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD

    private fun updateViewPosition(event: MotionEvent) {
        x = mOriginalX + event.rawX - mOriginalRawX
        // 限制不可超出屏幕高度
        var desY = mOriginalY + event.rawY - mOriginalRawY
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight.toFloat()
        }
        if (desY > mScreenHeight - height) {
            desY = (mScreenHeight - height).toFloat()
        }
        y = desY
    }

    private fun changeOriginalTouchParams(event: MotionEvent) {
        mOriginalX = x
        mOriginalY = y
        mOriginalRawX = event.rawX
        mOriginalRawY = event.rawY
        mLastTouchDownTime = System.currentTimeMillis()
    }

    private fun updateSize() {
        val viewGroup = parent as ViewGroup?
        viewGroup?.apply {
            mScreenWidth = this.width
            mScreenHeight = this.height
        }
//        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    @JvmOverloads
    fun moveToEdge(isLeft: Boolean = isNearestLeft(), isLandscape: Boolean = false) {
        val moveDistance = (if (isLeft) MARGIN_EDGE else mScreenWidth - width - MARGIN_EDGE).toFloat()
        var y = y
        if (!isLandscape && mPortraitY != 0f) {
            y = mPortraitY
            clearPortraitY()
        }
        mMoveAnimator.start(
            moveDistance, min(max(0.0, y.toDouble()), (mScreenHeight - height).toDouble())
                .toFloat()
        )
    }

    private fun clearPortraitY() {
        mPortraitY = 0f
    }

    private fun isNearestLeft(): Boolean {
        val middle = mScreenWidth / 2
        return x < middle
    }

    fun onRemove() {
        mMagnetViewListener?.onRemove(this)
    }

    protected inner class MoveAnimator : Runnable {
        private val handler = Handler(Looper.getMainLooper())
        private var destinationX = 0f
        private var destinationY = 0f
        private var startingTime: Long = 0
        fun start(x: Float, y: Float) {
            destinationX = x
            destinationY = y
            startingTime = System.currentTimeMillis()
            handler.post(this)
        }

        override fun run() {
            if (getRootView() == null || getRootView().parent == null) {
                return
            }
            val progress = min(1.0, ((System.currentTimeMillis() - startingTime) / 400f).toDouble())
                .toFloat()
            val deltaX = (destinationX - x) * progress
            val deltaY = (destinationY - y) * progress
            move(deltaX, deltaY)
            if (progress < 1) {
                handler.post(this)
            }
        }

        fun stop() {
            handler.removeCallbacks(this)
        }
    }

    private fun move(deltaX: Float, deltaY: Float) {
        x += deltaX
        y += deltaY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (parent != null) {
            val isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
            markPortraitY(isLandscape)
            (parent as ViewGroup).post {
                updateSize()
                moveToEdge(false, isLandscape)
            }
        }
    }

    private fun markPortraitY(isLandscape: Boolean) {
        if (isLandscape) {
            mPortraitY = y
        }
    }
}
