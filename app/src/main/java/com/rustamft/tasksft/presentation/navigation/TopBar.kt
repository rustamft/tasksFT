package com.rustamft.tasksft.presentation.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.element.IconButtonElement
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL

@Composable
fun TopBar(
    navigator: DestinationsNavigator,
    hasBackButton: Boolean,
    items: List<NavItem>
) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        elevation = DIMEN_SMALL,
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = {
            if (hasBackButton) {
                IconButtonElement(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.action_back),
                    onClick = { navigator.popBackStack() }
                )
            }
        },
        actions = {
            items.forEach { item ->
                IconButtonElement(
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
