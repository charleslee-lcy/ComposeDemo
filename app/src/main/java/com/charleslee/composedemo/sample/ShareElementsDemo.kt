@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class)

package com.charleslee.composedemo.sample

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charleslee.composedemo.R
import com.charleslee.composedemo.ui.theme.LavenderLight
import com.charleslee.composedemo.ui.theme.RoseLight


/**
 *
 * <p> Created by CharlesLee on 2025/2/11
 * 15708478830@163.com
 */
val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@Composable
fun SharedElement_ManualVisibleControl() {
    // [START android_compose_shared_element_manual_control]
    var selectFirst by remember { mutableStateOf(false) }
    val key = remember { Any() }
    SharedTransitionLayout(
        Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable {
                selectFirst = !selectFirst
            }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .sharedElementWithCallerManagedVisibility(
                        rememberSharedContentState(key = key),
                        !selectFirst
                    )
                    .background(Color.Red)
                    .size(100.dp)
            ) {
                Text(if (!selectFirst) "false" else "true", color = Color.White)
            }
            Box(
                Modifier
                    .offset(180.dp, 180.dp)
                    .sharedElementWithCallerManagedVisibility(
                        rememberSharedContentState(
                            key = key,
                        ),
                        selectFirst
                    )
                    .alpha(0.5f)
                    .background(Color.Blue)
                    .size(180.dp)
            ) {
                Text(if (selectFirst) "false" else "true", color = Color.White)
            }
        }
    }
    // [END android_compose_shared_element_manual_control]
}

@Composable
fun SharedElementScope_CompositionLocal() {
    var state by remember {
        mutableStateOf(false)
    }
    // An example of how to use composition locals to pass around the shared transition scope, far down your UI tree.
    // ...
    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            // This could also be your top-level NavHost as this provides an AnimatedContentScope
            AnimatedContent(state, label = "Top level AnimatedContent") { targetState ->
                CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                    // Now we can access the scopes in any nested composables as follows:
                    val sharedTransitionScope = LocalSharedTransitionScope.current
                        ?: throw IllegalStateException("No SharedElementScope found")
                    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
                        ?: throw IllegalStateException("No AnimatedVisibility found")
                    if (!targetState) {
                        MainContentGlobal(
                            onShowDetails = {
                                state = true
                            }
                        )
                    } else {
                        DetailsContentGlobal(
                            onBack = {
                                state = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainContentGlobal(
    onShowDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    with(LocalSharedTransitionScope.current!!) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .background(LavenderLight, RoundedCornerShape(8.dp))
                .clickable {
                    onShowDetails()
                }
                .padding(8.dp)
                .sharedBounds(
                    rememberSharedContentState(key = "bounds"),
                    animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current!!,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
        ) {
            val rememberSharedContentState = rememberSharedContentState(key = "image")
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Cupcake",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState,
                        animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current!!
                    )
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                "Cupcake", fontSize = 21.sp,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "title"),
                    animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current!!
                )
            )
        }
    }
}

@Composable
private fun DetailsContentGlobal(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    with(LocalSharedTransitionScope.current!!) {
        Column(
            modifier = Modifier
                .padding(top = 200.dp, start = 16.dp, end = 16.dp)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .background(RoseLight, RoundedCornerShape(8.dp))
                .clickable {
                    onBack()
                }
                .padding(8.dp)
                .sharedBounds(
                    rememberSharedContentState(key = "bounds"),
                    animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current!!,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Cupcake",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image"),
                        animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current!!
                    )
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                "Cupcake", fontSize = 28.sp,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "title"),
                    animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current!!
                )
            )
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet lobortis velit. " +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Curabitur sagittis, lectus posuere imperdiet facilisis, nibh massa " +
                        "molestie est, quis dapibus orci ligula non magna. Pellentesque rhoncus " +
                        "hendrerit massa quis ultricies. Curabitur congue ullamcorper leo, at maximus"
            )
        }
    }
}


@Preview
@Composable
fun SharedElementExample() {
    var showDetails by remember {
        mutableStateOf(false)
    }
    SharedTransitionLayout {
        AnimatedContent(
            showDetails,
            label = "basic_transition"
        ) { targetState ->
            if (!targetState) {
                MainContent(
                    onShowDetails = {
                        showDetails = true
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            } else {
                DetailsContent(
                    onBack = {
                        showDetails = false
                    },
                    animatedVisibilityScope = this@AnimatedContent,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    onShowDetails: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .background(LavenderLight, RoundedCornerShape(8.dp))
                .clickable {
                    onShowDetails()
                }
                .padding(8.dp)
                .sharedBounds(
                    rememberSharedContentState(key = "bounds"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
        ) {
            val rememberSharedContentState = rememberSharedContentState(key = "image")
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Cupcake",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                "Cupcake", fontSize = 21.sp,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "title"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }
    }
}

@Composable
private fun DetailsContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        Column(
            modifier = Modifier
                .padding(top = 200.dp, start = 16.dp, end = 16.dp)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .background(RoseLight, RoundedCornerShape(8.dp))
                .clickable {
                    onBack()
                }
                .padding(8.dp)
                .sharedBounds(
                    rememberSharedContentState(key = "bounds"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Cupcake",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                "Cupcake", fontSize = 28.sp,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "title"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur sit amet lobortis velit. " +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        " Curabitur sagittis, lectus posuere imperdiet facilisis, nibh massa " +
                        "molestie est, quis dapibus orci ligula non magna. Pellentesque rhoncus " +
                        "hendrerit massa quis ultricies. Curabitur congue ullamcorper leo, at maximus"
            )
        }
    }
}
