package com.rustamft.tasksft.presentation.screen.list

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.util.ROUTE_EDITOR
import com.rustamft.tasksft.domain.util.ROUTE_LIST
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
            FloatingActionButton(onClick = { navigator.navigate(ROUTE_EDITOR) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(id = R.string.action_add)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            itemsIndexed(listOfTasks) { _: Int, task: Task ->
                Card(
                    modifier = Modifier.padding(DIMEN_SMALL),
                    shape = Shapes.large
                ) {
                    Row {
                        Checkbox(
                            checked = task.isFinished,
                            onCheckedChange = { isChecked ->
                                task.isFinished = isChecked
                                viewModel.saveTask(task = task)
                            }
                        )
                        Text(text = task.title)
                    }
                }
            }
        }
    }
}
