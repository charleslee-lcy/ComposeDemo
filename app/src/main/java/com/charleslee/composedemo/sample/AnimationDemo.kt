package com.charleslee.composedemo.sample

import FlowBus
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.charleslee.composedemo.event.FlowConstant
import com.charleslee.composedemo.ui.theme.Pink80
import com.charleslee.composedemo.ui.theme.Purple40
import com.charleslee.composedemo.util.toPx
import kotlin.math.roundToInt


/**
 *
 * <p> Created by CharlesLee on 2025/2/10
 * 15708478830@163.com
 */
@Preview(showSystemUi = true)
@Composable
fun AnimationExample() {
    val scope = rememberCoroutineScope()
    var visible1 by remember {
        mutableStateOf(true)
    }
    var visible2 by remember {
        mutableStateOf(true)
    }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible2) 1.0f else 0f,
        label = "alpha"
    )

    var visible3 by remember {
        mutableStateOf(true)
    }
    val animatedColor by animateColorAsState(
        if (visible3) Color.Green else Color.Blue,
        label = "color"
    )
    var expanded by remember { mutableStateOf(false) }

    var moved by remember { mutableStateOf(false) }
    val pxToMove = 200.dp.toPx()
    val pyToMove = 300.dp.toPx()
    val offset by animateIntOffsetAsState(
        targetValue = if (moved) {
            IntOffset(pxToMove, -pyToMove)
        } else {
            IntOffset.Zero
        },
        label = "offset"
    )


    Column {
        Button(onClick = {
            visible1 = !visible1
        }) {
            Text("动画1")
        }
        Button(onClick = {
            visible2 = !visible2
        }) {
            Text("动画1")
        }
        Button(onClick = {
            visible3 = !visible3
        }) {
            Text("动画3")
        }
        AnimatedVisibility(
            visible1,
            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End),
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Cyan)
            )
        }
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(10.dp)
                .graphicsLayer {
                    alpha = animatedAlpha
                }
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Green)
                .align(Alignment.CenterHorizontally)
        )
        Box(modifier = Modifier
            .size(150.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(8.dp))
            .drawBehind {
                drawRect(animatedColor)
            }) {

        }
        Box(
            modifier = Modifier
                .background(Purple40)
                .padding(bottom = 30.dp)
                .animateContentSize(finishedListener = { _, _ ->
                    FlowBus.with<String>(FlowConstant.UPDATE_HEIGHT).post(scope, "")
                })
                .height(if (expanded) 400.dp else 200.dp)
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    expanded = !expanded
                }
        )
        Box(
            modifier = Modifier
                .offset {
                    offset
                }
                .background(Pink80)
                .size(100.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    moved = !moved
                }
        )
    }


}