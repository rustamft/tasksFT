package com.rustamft.tasksft.presentation.screen.list

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.BuildConfig
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences.Theme
import com.rustamft.tasksft.domain.model.Task
import com.rustamft.tasksft.presentation.element.TextButtonElement
import com.rustamft.tasksft.presentation.global.GITHUB_LINK
import com.rustamft.tasksft.presentation.global.ROUTE_EDITOR
import com.rustamft.tasksft.presentation.global.ROUTE_LIST
import com.rustamft.tasksft.presentation.global.ROUTE_SETTINGS
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_FAB
import com.rustamft.tasksft.presentation.global.TAG_LIST_SCREEN_TASK_CARD
import com.rustamft.tasksft.presentation.global.toDateTime
import com.rustamft.tasksft.presentation.model.TaskViewState
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.screen.destinations.EditorScreenDestination
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import com.rustamft.tasksft.presentation.theme.DIMEN_ZERO
import com.rustamft.tasksft.presentation.theme.Gray
import com.rustamft.tasksft.presentation.theme.Shapes
import com.rustamft.tasksft.presentation.theme.TEXT_SMALL
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Destination(start = true, route = ROUTE_LIST)
@Composable
fun ListScreen(
    navigator: DestinationsNavigator, // From ComposeDestinations
    scaffoldState: ScaffoldState, // From DependenciesContainer
    viewModel: ListViewModel = koinViewModel()
) {

    val listOfTasksState = viewModel.listOfTasksFlow.collectAsState(initial = emptyList())

    ListScreenContent(
        scaffoldState = scaffoldState,
        tasks = listOfTasksState,
        openAppInfoDialog = viewModel.openAppInfoDialogState,
        openGitHub = viewModel.openGitHubState,
        onNavigateToSettings = { navigator.navigate(ROUTE_SETTINGS) },
        onNavigateToEditorNewTask = { navigator.navigate(ROUTE_EDITOR) },
        onNavigateToEditorExistingTask = { id ->
            navigator.navigate(EditorScreenDestination(taskId = id))
        },
        onDeleteFinishedTasks = {
            viewModel.deleteTasks(
                tasks = listOfTasksState.value.filter { it.finished }
            )
        },
        onSaveTask = { task ->
            viewModel.saveTask(
                task = task.copy(finished = !task.finished)
            )
        }
    )
}

@Composable
private fun ListScreenContent(
    scaffoldState: ScaffoldState,
    tasks: State<List<Task>>,
    openAppInfoDialog: MutableState<Boolean>,
    openGitHub: MutableState<Boolean>,
    onNavigateToSettings: () -> Unit,
    onNavigateToEditorNewTask: () -> Unit,
    onNavigateToEditorExistingTask: (Int) -> Unit,
    onDeleteFinishedTasks: () -> Unit,
    onSaveTask: (Task) -> Unit,
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_LIST_SCREEN),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_clean,
                        descriptionResId = R.string.action_delete_finished,
                        onClick = onDeleteFinishedTasks
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_settings,
                        descriptionResId = R.string.action_settings,
                        onClick = onNavigateToSettings
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.app_info,
                        onClick = { openAppInfoDialog.value = true }
                    )
                )
            )
        },
        floatingActionButton = {
            Fab(
                modifier = Modifier.testTag(TAG_LIST_SCREEN_FAB),
                item = NavItem(
                    painterResId = R.drawable.ic_add,
                    descriptionResId = R.string.action_add,
                    onClick = onNavigateToEditorNewTask
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {

            items(
                items = tasks.value,
                key = { it.id }
            ) { task ->

                val (accentColor, secondaryColor) = if (task.finished) {
                    Color.Gray.copy(alpha = 0.3f) to Color.Gray
                } else {
                    Color(task.color) to AppTheme.colors.onBackground
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = DIMEN_SMALL)
                        .padding(horizontal = DIMEN_SMALL)
                        .pointerInput(key1 = task.finished) {
                            detectTapGestures(
                                onTap = { onSaveTask(task) },
                                onLongPress = { onNavigateToEditorExistingTask(task.id) }
                            )
                        }
                        .testTag(TAG_LIST_SCREEN_TASK_CARD),
                    shape = Shapes.large,
                    elevation = DIMEN_ZERO
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        accentColor.compositeOver(AppTheme.colors.background),
                                        AppTheme.colors.background
                                    )
                                )
                            )
                            .padding(all = DIMEN_SMALL),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f, false),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                if (task.finished) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_checked),
                                        contentDescription = stringResource(id = R.string.task_finished_state),
                                        colorFilter = ColorFilter.tint(AppTheme.colors.primary)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_unchecked),
                                        contentDescription = stringResource(id = R.string.task_finished_state),
                                        colorFilter = ColorFilter.tint(Gray)
                                    )
                                }
                            }
                            Column(modifier = Modifier.padding(horizontal = DIMEN_SMALL)) {
                                Text(text = task.title, maxLines = 2, color = secondaryColor)
                                if (task.description.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(DIMEN_SMALL))
                                    Text(
                                        text = task.description,
                                        maxLines = 3,
                                        fontSize = TEXT_SMALL,
                                        color = secondaryColor
                                    )
                                }
                            }
                        }
                        if (task.reminder != 0L) {
                            val textColor = accentColor.compositeOver(AppTheme.colors.onBackground)
                            Row(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .weight(0.5f, false),
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(horizontalAlignment = Alignment.End) {
                                    val dateTime = task.reminder.toDateTime()
                                    Text(
                                        text = dateTime.date,
                                        color = textColor
                                    )
                                    Text(
                                        text = dateTime.time,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    if (task.repeatCalendarUnit != 0) {
                                        TaskViewState
                                            .CALENDAR_UNIT_TO_NAME[task.repeatCalendarUnit]
                                            ?.let { unitNameUiText ->
                                                Text(
                                                    text = unitNameUiText.asString(),
                                                    color = textColor
                                                )
                                            }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (openAppInfoDialog.value) {
            AlertDialog(
                onDismissRequest = { openAppInfoDialog.value = false },
                title = { Text(text = stringResource(id = R.string.app_info)) },
                text = {
                    Text(
                        text = "${
                            stringResource(id = R.string.app_info_dialog_content)
                        } ${BuildConfig.VERSION_NAME}"
                    )
                },
                confirmButton = {
                    TextButtonElement(
                        onClick = { openAppInfoDialog.value = false },
                        text = stringResource(R.string.action_close)
                    )
                },
                dismissButton = {
                    TextButtonElement(
                        onClick = { openGitHub.value = true },
                        text = "GitHub"
                    )
                },
                backgroundColor = MaterialTheme.colors.surface
            )
        }

        if (openGitHub.value) {
            val uriHandler = LocalUriHandler.current
            val uri = Uri.parse(GITHUB_LINK)
            uriHandler.openUri(uri.toString())
            openGitHub.value = false
        }
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    AppTheme(theme = Theme.Dark) {
        ListScreenContent(
            scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState()),
            tasks = remember {
                mutableStateOf(
                    listOf(
                        "one" to "",
                        "two" to "some single line commentary",
                        "three" to "",
                        "four" to "another commentary\nhowever, it's longer than the previous one",
                        "five" to ""
                    ).mapIndexed { index, (title, description) ->
                        Task(
                            id = index,
                            created = 0L,
                            title = title,
                            description = description,
                            reminder = Calendar.getInstance().apply {
                                add(Calendar.DAY_OF_MONTH, index)
                                add(Calendar.HOUR_OF_DAY, index)
                                add(Calendar.MINUTE, index * 10)
                            }.timeInMillis,
                            repeatCalendarUnit = 0,
                            finished = false,
                            color = AppTheme.taskColors[index].toArgb()
                        )
                    }
                )
            },
            openAppInfoDialog = remember { mutableStateOf(false) },
            openGitHub = remember { mutableStateOf(false) },
            onNavigateToSettings = {},
            onNavigateToEditorNewTask = {},
            onNavigateToEditorExistingTask = {},
            onDeleteFinishedTasks = {},
            onSaveTask = {}
        )
    }
}
