package com.rustamft.tasksft.presentation.screen.list

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.app.App
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.domain.util.GITHUB_LINK
import com.rustamft.tasksft.domain.util.ROUTE_LIST
import com.rustamft.tasksft.domain.util.toDateTime
import com.rustamft.tasksft.presentation.element.TextButtonElement
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.screen.destinations.EditorScreenDestination
import com.rustamft.tasksft.presentation.screen.editor.EditorScreenNavArgs
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import com.rustamft.tasksft.presentation.theme.Shapes
import com.rustamft.tasksft.presentation.theme.TEXT_SMALL

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
    var openDialog by remember { mutableStateOf(false) }
    var openGitHub by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel) {
        viewModel.errorFlow.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navigator = navigator,
                hasBackButton = false,
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_clean,
                        descriptionResId = R.string.action_delete_finished,
                        onClick = {
                            viewModel.deleteTasks(
                                list = listOfTasks.filter { it.isFinished }
                            )
                        }
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.app_info,
                        onClick = { openDialog = true }
                    )
                )
            )
        },
        floatingActionButton = {
            Fab(
                item = NavItem(
                    painterResId = R.drawable.ic_add,
                    descriptionResId = R.string.action_add,
                    onClick = {
                        val navArgs = EditorScreenNavArgs(indexOfTaskInList = -1)
                        navigator.navigate(EditorScreenDestination(navArgs))
                    }
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            itemsIndexed(listOfTasks) { index: Int, task: Task ->

                val onTap = {
                    //viewModel.saveTask(task = task.copy(isFinished = !task.isFinished))
                    viewModel.saveTask(
                        task = task.copy(isFinished = !task.isFinished)
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(DIMEN_SMALL)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onTap() }, // TODO: after quick taps stops reacting
                                onLongPress = {
                                    val navArgs = EditorScreenNavArgs(
                                        indexOfTaskInList = index
                                    )
                                    navigator.navigate(EditorScreenDestination(navArgs))
                                }
                            )
                        },
                    shape = Shapes.large
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(DIMEN_SMALL),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f, false),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                if (task.isFinished) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_checked),
                                        contentDescription = stringResource(id = R.string.task_finished_state),
                                        colorFilter = ColorFilter.tint(Color.Cyan)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_unchecked),
                                        contentDescription = stringResource(id = R.string.task_finished_state),
                                        colorFilter = ColorFilter.tint(Color.Gray)
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(horizontal = DIMEN_SMALL)) {
                                Text(text = task.title, maxLines = 2)
                                if (task.description.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(DIMEN_SMALL))
                                    Text(
                                        text = task.description,
                                        maxLines = 3,
                                        fontSize = TEXT_SMALL
                                    )
                                }
                            }
                        }
                        if (task.reminder != 0L) {
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .weight(0.5f, false),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    val dateTime = task.reminder.toDateTime()
                                    Text(text = dateTime.date)
                                    Text(text = dateTime.time, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                title = { Text(text = stringResource(id = R.string.app_info)) },
                text = {
                    Text(text = "${stringResource(id = R.string.app_info_content)} ${App.version}")
                },
                confirmButton = {
                    TextButtonElement(
                        onClick = { openDialog = false },
                        text = stringResource(R.string.action_close)
                    )
                },
                dismissButton = {
                    TextButtonElement(
                        onClick = { openGitHub = true },
                        text = "GitHub"
                    )
                },
                backgroundColor = MaterialTheme.colors.background
            )
        }

        if (openGitHub) {
            val uriHandler = LocalUriHandler.current
            val uri = Uri.parse(GITHUB_LINK)
            uriHandler.openUri(uri.toString())
            openGitHub = false
        }
    }
}
