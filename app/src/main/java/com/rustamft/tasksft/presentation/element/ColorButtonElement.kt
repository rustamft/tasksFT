package com.rustamft.tasksft.presentation.element

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
import androidx.compose.ui.tooling.preview.Preview
import com.rustamft.tasksft.domain.model.Preferences.Theme
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.BORDER_SMALL
import com.rustamft.tasksft.presentation.theme.DIMEN_MEDIUM

@Composable
fun ColorButtonElement(
    color: Color,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(size = DIMEN_MEDIUM)
            .clip(shape = CircleShape)
            .border(
                width = BORDER_SMALL,
                color = AppTheme.colors.onBackground,
                shape = CircleShape
            )
            .background(color = color)
            .clickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun ColorButtonElementPreview() {
    AppTheme(theme = Theme.Dark) {
        ColorButtonElement(
            color = AppTheme.taskColors[0],
            onClick = {}
        )
    }
}
