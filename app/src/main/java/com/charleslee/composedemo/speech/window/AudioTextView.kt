package com.charleslee.composedemo.speech.window

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


/**
 *
 * <p> Created by CharlesLee on 2022/7/27
 * 15708478830@163.com
 */
class AudioTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatTextView(context, attrs, defStyleAttr) {
    private var isFirst = true
    private var fixedText = ""
    private var lineThreeEndIndex = 0

//    companion object {
//        var normalTypeFace: Typeface? = null
//        @JvmStatic
//        fun findNormalTypeFace(context : Context): Typeface? {
//            if (normalTypeFace == null) {
//                normalTypeFace = ResourcesCompat.getFont(context, R.font.custom_font)
//            }
//            return normalTypeFace
//        }
//    }

    constructor(context: Context): this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        initView()
    }

    private fun initView() {
        viewTreeObserver.addOnGlobalLayoutListener {
            if (this@AudioTextView.lineCount > 3) {
                if (isFirst) {
                    // 第一次超过三行记录固定文字内容
                    isFirst = false
                    lineThreeEndIndex = layout.getLineEnd(2)
                }

                // 后续截取最新文字的后8个字符拼接到省略号后
                val result = "...${
                    text.subSequence(
                        text.length - lineThreeEndIndex + 1,
                        text.length
                    )
                }"
                text = result
            }
        }

//        typeface = findNormalTypeFace(context)
    }
}