package com.rustamft.tasksft.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rustamft.tasksft.presentation.theme.AppTheme

@Composable
fun Fab(
    modifier: Modifier = Modifier,
    item: NavItem,
    visibilityState: State<Boolean> = remember { mutableStateOf(true) }
) {
    AnimatedVisibility(
        visible = visibilityState.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        FloatingActionButton(
            modifier = modifier,
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
}
