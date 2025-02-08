package com.charleslee.composedemo.sample

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Vertical
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.charleslee.composedemo.R
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items

/**
 * 列表示例
 * <p> Created by CharlesLee on 2025/2/8
 * 15708478830@163.com
 */

// 定义常量来替代魔数
const val GRID_COLUMN_COUNT = 4
val GRID_VERTICAL_SPACING_DP = 5.dp
val GRID_HORIZONTAL_SPACING_DP = 5.dp

@Composable
fun ListExample(listData: List<String>) {
//    LazyColumnDemo(listData)
    LazyVerticalGridDemo(listData)
}

@Composable
private fun LazyVerticalGridDemo(listData: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_COLUMN_COUNT),
        verticalArrangement = Arrangement.spacedBy(GRID_VERTICAL_SPACING_DP),  // 设置垂直间距
        horizontalArrangement = Arrangement.spacedBy(GRID_HORIZONTAL_SPACING_DP)  // 设置水平间距
    ) {
        items(listData) { item ->
            ListItem(item)
        }
    }
}

@Composable
private fun ListItem(text: String) {
    Card(onClick = {
        Log.i("ListItem", "click item $text")
    }) {
        Image(painter = painterResource(R.drawable.ic_launcher_foreground), "")
        Text("item $text", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}

@Composable
private fun LazyColumnDemo(listData: List<String>) {
    LazyColumn {
        itemsIndexed(listData) { index, item ->
            Text(text = "- List item $item number ${index + 1}")
        }
    }
}

@Composable
private fun ScrollDemo() {
    val verticalScrollState = rememberScrollState()
    val verticalScrollableState = rememberScrollableState { delta ->
        Log.d("delta", "delta: $delta")
        delta
    }
    Column(
        modifier = Modifier
            .scrollable(verticalScrollableState, Orientation.Vertical)
            .verticalScroll(verticalScrollState)
    ) {
        val range = 1..100

        range.forEach {
            Text(text = "- List item number ${it + 1}")
        }
    }
}