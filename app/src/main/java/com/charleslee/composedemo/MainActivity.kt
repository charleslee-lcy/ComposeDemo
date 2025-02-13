@file:OptIn(ExperimentalMaterial3Api::class)

package com.charleslee.composedemo

import FlowBus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.G
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.charleslee.composedemo.base.BaseContent
import com.charleslee.composedemo.base.BaseTopBar
import com.charleslee.composedemo.base.SCROLL_TYPE_ENTER_ALWAYS
import com.charleslee.composedemo.event.FlowConstant
import com.charleslee.composedemo.sample.AnimatedVisibilitySharedElementBlurLayer
import com.charleslee.composedemo.sample.AnimatedVisibilitySharedElementShortenedExample
import com.charleslee.composedemo.sample.AnimationExample
import com.charleslee.composedemo.sample.MediumTopAppBarExample
import com.charleslee.composedemo.sample.ScrollContent
import com.charleslee.composedemo.sample.SharedElementExample
import com.charleslee.composedemo.sample.SharedElementScope_CompositionLocal
import com.charleslee.composedemo.sample.SharedElement_ManualVisibleControl
import com.charleslee.composedemo.sample.TextExample
import com.charleslee.composedemo.ui.theme.CommonTheme
import com.charleslee.composedemo.ui.theme.DarkScrim
import com.charleslee.composedemo.ui.theme.LightScrim
import com.charleslee.composedemo.ui.theme.Purple40
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CommonTheme(isImmersive = false) {
                PageContent()
            }
        }
    }
}

@Composable
fun PageContent() {
    BaseContent(
        topBar = {
            BaseTopBar(
                title = "我的页面",
                scrollBehavior = it
            )
        },
        scrollType = SCROLL_TYPE_ENTER_ALWAYS,
    ) {
        ScrollContent(it)
    }
}

@Composable
fun Greeting(name: String) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
//    AnimatedVisibilitySharedElementShortenedExample()
//    AnimatedVisibilitySharedElementBlurLayer()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        MediumTopAppBarExample()
//        ConstraintLayoutExample()
//        BadgeInteractiveExample()
//        BottomSheetExample()
//        ChipExample()
//        testDialog()
//        DrawerExample {
//            MediumTopAppBarExample()
//        }
//        LoginScreen()
//        val data = 1..100
//        ListExample(data.map {
//            it.toString()
//        })

//        TextExample()
//        AnimationExample()
//        SharedElementExample()
//        SharedElementScope_CompositionLocal()
//        SharedElement_ManualVisibleControl()
    }

    if (LocalContext.current is ComponentActivity) {
        val context = LocalContext.current as ComponentActivity
        FlowBus.with<String>(FlowConstant.UPDATE_HEIGHT).register(context) {
            scope.launch {
                scrollState.scrollTo(scrollState.maxValue)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    PageContent()
}