package com.charleslee.composedemo.speech.window

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.charleslee.composedemo.R
import com.charleslee.composedemo.databinding.EnFloatingViewBinding

class EnFloatingView @JvmOverloads constructor(
    context: Context,
    @LayoutRes resource: Int = R.layout.en_floating_view
) : FloatingMagnetView(context, null) {
    var binding: EnFloatingViewBinding

    init {
        binding = EnFloatingViewBinding.inflate(LayoutInflater.from(context), this, true)
//        inflate(context, resource, this)
    }

    fun setIconImage(@DrawableRes resId: Int) {
        binding.icon.setImageResource(resId)
    }
}
