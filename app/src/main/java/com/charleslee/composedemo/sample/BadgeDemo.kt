package com.charleslee.composedemo.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/**
 * 标签（消息数，任务状态等）
 * <p> Created by CharlesLee on 2025/2/7
 * 15708478830@163.com
 */
@Composable
fun BadgeExample() {
    Row(modifier = Modifier.fillMaxWidth()) {
        BadgedBox(
            badge = {
                Badge(containerColor = Color.Red)
            }) {
            Icon(
                imageVector = Icons.Filled.MailOutline,
                contentDescription = "Menu"
            )
        }
        BadgedBox(
            badge = {
                Badge(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text("1")
                }
            }, modifier = Modifier.padding(start = 10.dp)) {
            Icon(
                imageVector = Icons.Filled.MailOutline,
                contentDescription = "Menu"
            )
        }
        BadgedBox(
            badge = {
                Badge(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text("10")
                }
            }, modifier = Modifier.padding(start = 10.dp)) {
            Icon(
                imageVector = Icons.Filled.MailOutline,
                contentDescription = "Menu"
            )
        }

        BadgedBox(
            badge = {
                Badge(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text("100")
                }
            }, modifier = Modifier.padding(start = 10.dp)) {
            Icon(
                imageVector = Icons.Filled.MailOutline,
                contentDescription = "Menu"
            )
        }
    }
}

@Composable
fun BadgeLabel(text: String = "") {
    Badge(
        containerColor = Color.Red,
        contentColor = Color.White
    ) {
        Text(text)
    }
}

@Composable
fun BadgeInteractiveExample() {
    var itemCount by remember { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BadgedBox(
            badge = {
                if (itemCount > 0) {
//                    Badge(
//                        containerColor = Color.Red,
//                        contentColor = Color.White
//                    ) {
//                        Text("$itemCount")
//                    }
                    BadgeLabel("已选$itemCount")
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Shopping cart",
            )
        }
        Button(onClick = { itemCount++ }) {
            Text("Add item")
        }
    }
}