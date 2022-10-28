package com.rustamft.tasksft.presentation.element

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun IconButtonElement(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {

    IconButton(onClick = onClick) {
        Icon(
            painter = painter,
            contentDescription = contentDescription
        )
    }
}
