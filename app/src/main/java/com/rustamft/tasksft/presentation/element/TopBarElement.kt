package com.rustamft.tasksft.presentation.element

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL

@Composable
fun TopBarElement(actions: @Composable (RowScope.() -> Unit)) {

    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        modifier = Modifier.fillMaxWidth(),
        elevation = DIMEN_SMALL,
        actions = actions,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    )
}
