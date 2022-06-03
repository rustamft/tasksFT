package com.rustamft.tasksft.presentation.screen.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.util.ROUTE_EDITOR

@Destination(
    route = ROUTE_EDITOR,
    navArgsDelegate = EditorScreenNavArgs::class
)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState, // From DependenciesContainer.
    taskState: State<Task> = viewModel.taskFlow.collectAsState(initial = Task())
) {

    var fabVisible by remember { mutableStateOf(false) }
    val onValueChange = { fabVisible = true }
    val task by taskState
    // TODO: make the text field values change

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
                        painter = painterResource(id = R.drawable.ic_add), // TODO: change
                        contentDescription = stringResource(id = R.string.action_add) // TODO: change
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            TextField(
                value = task.title,
                onValueChange = {
                    task.title = it
                    onValueChange()
                },
                placeholder = { Text(text = stringResource(id = R.string.title)) }
            )
            TextField(
                value = task.description,
                onValueChange = {
                    task.description = it
                    onValueChange()
                },
                placeholder = { Text(text = stringResource(id = R.string.description)) }
            )
        }
    }
}
