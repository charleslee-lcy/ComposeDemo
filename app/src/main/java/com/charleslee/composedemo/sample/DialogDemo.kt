package com.charleslee.composedemo.sample

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.charleslee.composedemo.R


/**
 * 对话框示例
 * <p> Created by CharlesLee on 2025/2/7
 * 15708478830@163.com
 */
@Composable
fun testDialog(modifier: Modifier = Modifier) {
    val openAlertDialog = remember { mutableStateOf(false) }

    FloatingActionButton(onClick = {
        openAlertDialog.value = true
    }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Localized description"
        )
    }

    MinimalDropdownMenu(modifier)
    LongBasicDropdownMenu()
    DropdownMenuWithDetails()

//    if (openAlertDialog.value) {
//        AlertDialogExample(
//            onConfirm = {
//                openAlertDialog.value = false
//                println("Confirmation registered") // Add logic here to handle confirmation.
//            },
//            onDismiss = { openAlertDialog.value = false },
//            dialogTitle = "Alert dialog example",
//            dialogText = "This is an example of an alert dialog with buttons.",
//            icon = Icons.Default.Info
//        )
//    }

    if (openAlertDialog.value) {
        CustomDialogExample(
            onConfirm = {
                openAlertDialog.value = false
                println("Confirmation registered") // Add logic here to handle confirmation.
            },
            onDismiss = { openAlertDialog.value = false },
            dismissOnTouchOutside = false
        ) {
            ChipExample()
        }
    }
}

@Composable
fun AlertDialogExample(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    dismissOnTouchOutside: Boolean = true,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            if (dismissOnTouchOutside) {
                onDismiss()
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun CustomDialogExample(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissOnTouchOutside: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = { if (dismissOnTouchOutside) onDismiss() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirm() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun MinimalDropdownMenu(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Option 1") },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Option 2") },
                onClick = { /* Do something... */ }
            )
        }
    }
}

@Composable
fun LongBasicDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    // Placeholder list of 100 strings for demonstration
    val menuItemData = List(100) { "Option ${it + 1}" }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            menuItemData.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { /* Do something... */ }
                )
            }
        }
    }
}

@Composable
fun DropdownMenuWithDetails() {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // First section
            DropdownMenuItem(
                text = { Text("Profile") },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Settings") },
                leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

            HorizontalDivider()

            // Second section
            DropdownMenuItem(
                text = { Text("Send Feedback") },
                leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null) },
                onClick = { /* Do something... */ }
            )

            HorizontalDivider()

            // Third section
            DropdownMenuItem(
                text = { Text("About") },
                leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Help") },
                leadingIcon = { Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null) },
                trailingIcon = { Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
        }
    }
}