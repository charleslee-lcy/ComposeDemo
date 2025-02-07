package com.charleslee.composedemo.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.charleslee.composedemo.R


/**
 *
 * <p> Created by CharlesLee on 2025/2/7
 * 15708478830@163.com
 */
@Composable
fun ConstraintLayoutExample() {
    ConstraintLayout (
        modifier = Modifier.fillMaxSize()
    ) {
        val (space, icon, name, desc, image1, image2, image3, image4) = createRefs()

        val horizontalChain = createHorizontalChain(image1, image2, image3, image4, chainStyle = ChainStyle.SpreadInside)

        Spacer(Modifier.height(30.dp).constrainAs(space) {
            top.linkTo(parent.top)
        })

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.Green)
                .constrainAs(icon) {
                    start.linkTo(parent.start, margin = 15.dp)
                    top.linkTo(space.bottom, margin = 12.dp)
                },
            contentDescription = "",
        )

        Text("长直发的她",
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF333333),
            modifier = Modifier.constrainAs(name) {
                top.linkTo(icon.top, 0.dp)
                start.linkTo(icon.end, 10.dp)
            })

        Text("这个人很懒没有发表动态",
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF999999),
            modifier = Modifier.constrainAs(desc) {
                top.linkTo(name.bottom, 0.dp)
                start.linkTo(icon.end, 10.dp)
            })

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .padding(start = 15.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Blue)
                .constrainAs(image1) {
                    start.linkTo(parent.start, margin = 15.dp)
                    top.linkTo(desc.bottom, margin = 20.dp)
                },
            contentDescription = "",
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Red)
                .constrainAs(image2) {
                    top.linkTo(image1.top)
                },
            contentDescription = "",
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Yellow)
                .constrainAs(image3) {
                    top.linkTo(image1.top)
                },
            contentDescription = "",
        )

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .padding(end = 15.dp)
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Cyan)
                .constrainAs(image4) {
                    top.linkTo(image1.top)
                },
            contentDescription = "",
        )
    }
}