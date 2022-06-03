package com.rustamft.tasksft.presentation.screen.list

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.util.ROUTE_LIST
import com.rustamft.tasksft.presentation.screen.destinations.EditorScreenDestination
import com.rustamft.tasksft.presentation.screen.editor.EditorScreenNavArgs
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import com.rustamft.tasksft.presentation.theme.Shapes

@Destination(start = true, route = ROUTE_LIST)
@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState, // From DependenciesContainer.
    listOfTasksState: State<List<Task>> =
        viewModel.listOfTasksFlow.collectAsState(initial = emptyList())
) {

    val listOfTasks by listOfTasksState

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val navArgs = EditorScreenNavArgs(indexOfTaskInList = -1)
                    navigator.navigate(EditorScreenDestination(navArgs))
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(id = R.string.action_add)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            itemsIndexed(listOfTasks) { index: Int, task: Task ->

                val onClick = {
                    viewModel.saveTask(task = task.apply { isFinished = !isFinished })
                }

                Card(
                    modifier = Modifier
                        .padding(DIMEN_SMALL)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onClick() },
                                onLongPress = {
                                    val navArgs = EditorScreenNavArgs(indexOfTaskInList = index)
                                    navigator.navigate(EditorScreenDestination(navArgs))
                                }
                            )
                        },
                    shape = Shapes.large
                ) {
                    Row {
                        Checkbox(
                            checked = task.isFinished,
                            onCheckedChange = { onClick() }
                        )
                        Text(text = task.title)
                    }
                }
            }
        }
    }
}
