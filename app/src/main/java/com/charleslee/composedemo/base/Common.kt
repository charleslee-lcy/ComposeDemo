@file:OptIn(ExperimentalMaterial3Api::class)

package com.charleslee.composedemo.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.charleslee.composedemo.R


/**
 * <p> Created by CharlesLee on 2025/2/13
 * 15708478830@163.com
 */

const val SCROLL_TYPE_PINNED = 0
const val SCROLL_TYPE_ENTER_ALWAYS = 1
const val SCROLL_TYPE_EXIT_UNTIL_COLLAPSED = 2

@Composable
fun BaseContent(
    modifier: Modifier = Modifier,
    topBar: @Composable (topBarBehavior: TopAppBarScrollBehavior) -> Unit = { topBarBehavior -> BaseTopBar(scrollBehavior = topBarBehavior) },
    bottomBar: @Composable () -> Unit = {},
    contentColor: Color = Color.White,
    scrollType: Int = SCROLL_TYPE_PINNED,
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = when(scrollType) {
        SCROLL_TYPE_ENTER_ALWAYS -> TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        SCROLL_TYPE_EXIT_UNTIL_COLLAPSED -> TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        else -> TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            topBar(scrollBehavior)
        },
        containerColor = contentColor
    ) { innerPadding ->
        content(innerPadding)
    }
}

@Composable
fun BaseTopBar(
    title: String = "",
    titleColor: Color = Color(0xFF333333),
    backIcon: Painter = painterResource(R.drawable.ic_back),
    backOnClick: () -> Unit = {},
    actionIcon: Painter? = null,
    actionOnClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0, 0, 0, 0),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White,
            titleContentColor = titleColor,
        ),
        title = {
            Text(
                title,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = backOnClick) {
                Icon(
                    backIcon,
                    contentDescription = "title"
                )
            }
        },
        actions = {
            actionIcon?.apply {
                IconButton(onClick = actionOnClick) {
                    Icon(
                        this,
                        contentDescription = "content"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}