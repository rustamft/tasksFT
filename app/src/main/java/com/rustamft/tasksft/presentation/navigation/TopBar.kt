package com.rustamft.tasksft.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.composable.IconButtonComposable
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL

@Composable
fun TopBar(
    backButton: (@Composable () -> Unit)? = null,
    items: List<NavItem>
) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        elevation = DIMEN_SMALL,
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = backButton,
        actions = {
            items.forEach { item ->
                IconButtonComposable(
                    painter = painterResource(id = item.painterResId),
                    contentDescription = stringResource(id = item.descriptionResId),
                    onClick = item.onClick
                )
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    )
}
