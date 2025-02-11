package com.charleslee.composedemo

import FlowBus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charleslee.composedemo.event.FlowConstant
import com.charleslee.composedemo.sample.AnimatedVisibilitySharedElementBlurLayer
import com.charleslee.composedemo.sample.AnimatedVisibilitySharedElementShortenedExample
import com.charleslee.composedemo.sample.AnimationExample
import com.charleslee.composedemo.sample.SharedElementExample
import com.charleslee.composedemo.sample.SharedElementScope_CompositionLocal
import com.charleslee.composedemo.sample.SharedElement_ManualVisibleControl
import com.charleslee.composedemo.ui.theme.ComposeDemoTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Greeting("Android")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
//    AnimatedVisibilitySharedElementShortenedExample()
//    AnimatedVisibilitySharedElementBlurLayer()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(start = 14.dp, end = 14.dp)
    ) {
//        Spacer(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(30.dp)
//        )
//        MediumTopAppBarExample()
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
        SharedElement_ManualVisibleControl()
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
    ComposeDemoTheme {
        Greeting("Android")
    }
}