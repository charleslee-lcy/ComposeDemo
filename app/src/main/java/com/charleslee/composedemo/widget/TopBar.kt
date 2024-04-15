package com.charleslee.composedemo.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


/**
 *
 * <p> Created by CharlesLee on 2024/2/23
 * 15708478830@163.com
 */
@Preview(showSystemUi = true)
@Composable
fun TopBar() {
    Column {
        FilledIconButton(onClick = { /*TODO*/ }) {
            Text(text = "123")
        }
        FilledTonalButton(onClick = { /*TODO*/ }) {
            Text(text = "123")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "123")
        }
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text(text = "123")
        }
        ElevatedButton(
            onClick = { /*TODO*/ },
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 5.dp
            )
        ) {
            Text(text = "123")
        }
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "123")
        }
    }
}