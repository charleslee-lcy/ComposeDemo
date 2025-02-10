package com.charleslee.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charleslee.composedemo.sample.AlertDialogExample
import com.charleslee.composedemo.sample.BadgeInteractiveExample
import com.charleslee.composedemo.sample.BottomSheetExample
import com.charleslee.composedemo.sample.ChipExample
import com.charleslee.composedemo.sample.ConstraintLayoutExample
import com.charleslee.composedemo.sample.DrawerExample
import com.charleslee.composedemo.sample.ListExample
import com.charleslee.composedemo.sample.LoginScreen
import com.charleslee.composedemo.sample.MediumTopAppBarExample
import com.charleslee.composedemo.sample.TextExample
import com.charleslee.composedemo.sample.testDialog
import com.charleslee.composedemo.ui.theme.ComposeDemoTheme

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
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
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

        TextExample()
    }
}

@Preview(showSystemUi = true, device = PIXEL_4)
@Composable
fun GreetingPreview() {
    ComposeDemoTheme {
        Greeting("Android")
    }
}