package com.rustamft.tasksft.presentation.composable

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun TextButtonComposable(
    onClick: () -> Unit,
    text: String,
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape
    ) {
        Text(text = text)
    }
}
