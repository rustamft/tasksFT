package com.rustamft.tasksft.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.BORDER_SMALL
import com.rustamft.tasksft.presentation.theme.DIMEN_MEDIUM

@Composable
fun ColorButtonComposable(
    color: Color,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(
                width = BORDER_SMALL,
                color = AppTheme.colors.onBackground,
                shape = CircleShape
            )
            .size(DIMEN_MEDIUM)
            .clip(CircleShape)
            .background(color)
    )
}
