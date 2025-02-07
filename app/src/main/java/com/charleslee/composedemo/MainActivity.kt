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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.charleslee.composedemo.sample.BadgeExample
import com.charleslee.composedemo.sample.BadgeInteractiveExample
import com.charleslee.composedemo.sample.ConstraintLayoutExample
import com.charleslee.composedemo.sample.MediumTopAppBarExample
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
    Column(modifier = Modifier.fillMaxSize().padding(start = 14.dp, end = 14.dp)) {
        Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))
//    MediumTopAppBarExample()
//    ConstraintLayoutExample()
        BadgeExample()
        Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))
        BadgeInteractiveExample()
    }
}

@Preview(showSystemUi = true, device = PIXEL_4)
@Composable
fun GreetingPreview() {
    ComposeDemoTheme {
        Greeting("Android")
    }
}