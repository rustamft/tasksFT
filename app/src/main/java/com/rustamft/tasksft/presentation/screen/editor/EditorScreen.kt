package com.rustamft.tasksft.presentation.screen.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.util.ROUTE_EDITOR
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL

@Destination(
    route = ROUTE_EDITOR,
    navArgsDelegate = EditorScreenNavArgs::class
)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState // From DependenciesContainer.
) {

    var fabVisible by remember { mutableStateOf(false) }
    var reminderSelectorVisible by remember { mutableStateOf(viewModel.reminder != 0L) }
    val onValueChange = { fabVisible = true }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        floatingActionButton = {
            if (fabVisible) {
                FloatingActionButton(
                    onClick = {
                        // TODO: implement saving
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = stringResource(id = R.string.action_save)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {

            val modifier = Modifier
                .padding(DIMEN_SMALL)

            TextField(
                modifier = modifier,
                value = viewModel.title,
                onValueChange = {
                    viewModel.title = it
                    onValueChange()
                },
                placeholder = { Text(text = stringResource(id = R.string.title)) }
            )
            TextField(
                modifier = modifier,
                value = viewModel.description,
                onValueChange = {
                    viewModel.description = it
                    onValueChange()
                },
                placeholder = { Text(text = stringResource(id = R.string.description)) }
            )
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.reminder))
                Spacer(modifier = Modifier.width(DIMEN_SMALL))
                Switch(
                    checked = reminderSelectorVisible,
                    onCheckedChange = {
                        reminderSelectorVisible = it
                        onValueChange()
                    }
                )
            }
            if (reminderSelectorVisible) {
                Row(modifier = modifier) {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Date")
                    }
                    Spacer(modifier = Modifier.width(DIMEN_SMALL))
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Time")
                    }
                }
            }
        }
    }
}
