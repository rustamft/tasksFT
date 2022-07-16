package com.rustamft.tasksft.presentation.navigation

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
fun Fab(item: NavItem) {
    FloatingActionButton(
        onClick = item.onClick,
        content = {
            Icon(
                painter = painterResource(id = item.painterResId),
                contentDescription = stringResource(id = item.descriptionResId)
            )
        },
        contentColor = AppTheme.colors.secondaryVariant,
        backgroundColor = AppTheme.colors.primaryVariant
    )
}
