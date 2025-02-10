package com.charleslee.composedemo.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt


/**
 *
 * <p> Created by CharlesLee on 2025/2/10
 * 15708478830@163.com
 */
@Composable
fun Dp.toPx(): Int {
    var result = 0
    with(LocalDensity.current) {
        result = this@toPx.toPx().roundToInt()
    }
    return result
}