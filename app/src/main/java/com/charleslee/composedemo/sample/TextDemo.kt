package com.charleslee.composedemo.sample

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charleslee.composedemo.R
import com.charleslee.composedemo.ui.theme.Pink80
import com.charleslee.composedemo.ui.theme.Purple40
import com.charleslee.composedemo.ui.theme.Purple80


/**
 *
 * <p> Created by CharlesLee on 2025/2/10
 * 15708478830@163.com
 */
@Composable
fun TextExample() {
    val gradientColors = listOf(Pink80, Purple80, Purple40)
    val brush = Brush.linearGradient(colors = gradientColors)

    Text(
        "Do not allow people to dim your shine because they are blinded. Tell them to put some sunglasses on.",
        fontSize = 18.sp,
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = gradientColors
            )
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = buildAnnotatedString {
            append("这是我生命中的第")
//            addStyle(
//                style = SpanStyle(
//                    color = Color.Red
//                ),
//                start = 0,
//                end = 2
//            )
            withStyle(
                SpanStyle(
                    color = Color.Red,
                    fontSize = 30.sp
                )
            ) {
                append(" 8 ")
            }
            append("个日子")
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = AnnotatedString.fromHtml(
            "<h1>Jetpack Compose</h1>\n" +
                    "       <p>\n" +
                    "           Build <b>better apps</b> faster with <a href=\"https://www.android.com\">Jetpack Compose</a>\n" +
                    "       </p>",
            linkStyles = TextLinkStyles(
                style = SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    fontStyle = FontStyle.Italic,
                    color = Color.Blue
                )
            )
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    brush = brush, alpha = .5f
                )
            ) {
                append("Text in ")
            }
            withStyle(
                SpanStyle(
                    brush = brush, alpha = 1f
                )
            ) {
                append("Compose ❤️")
            }
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
        "Do not allow people to dim your shine because they are blinded. Tell them to put some sunglasses on.",
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = gradientColors
            )
        ),
        modifier = Modifier . basicMarquee (),
        fontSize = 30.sp
    )
    Spacer(modifier = Modifier.height(10.dp))
    var text by remember {
        mutableStateOf("hello")
    }
    var password by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
        },
        label = {
            Text(text = "Label")
        }
    )
    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it
        },
        label = {
            Text(text = "Password")
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(modifier = Modifier.height(10.dp))
    // Display multiple links in the text
    Text(
        buildAnnotatedString {
            append("Go to the ")
            withLink(
                LinkAnnotation.Url(
                    "https://developer.android.com/",
                    TextLinkStyles(style = SpanStyle(color = Color.Blue))
                )
            ) {
                append("Android Developers ")
            }
            append("website, and check out the")
            withLink(
                LinkAnnotation.Url(
                    "https://developer.android.com/jetpack/compose",
                    TextLinkStyles(style = SpanStyle(color = Color.Green))
                )
            ) {
                append("Compose guidance")
            }
            append(".")
        }
    )

    val uriHandler = LocalUriHandler.current
    Text(
        buildAnnotatedString {
            append("Build better apps faster with ")
            val link =
                LinkAnnotation.Url(
                    "https://developer.android.com/jetpack/compose",
                    TextLinkStyles(SpanStyle(color = Color.Blue))
                ) {
                    val url = (it as LinkAnnotation.Url).url
                    // log some metrics
                    uriHandler.openUri(url)
                }
            withLink(link) { append("Jetpack Compose") }
        }
    )




}