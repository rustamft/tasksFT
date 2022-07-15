package com.rustamft.tasksft.presentation.composable

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun IconButtonComposable(
    painter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {

    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription
        )
    }
}
