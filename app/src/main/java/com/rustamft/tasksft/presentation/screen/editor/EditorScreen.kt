package com.rustamft.tasksft.presentation.screen.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.FULL_ROUTE_PLACEHOLDER
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences.Theme
import com.rustamft.tasksft.presentation.element.ColorButtonElement
import com.rustamft.tasksft.presentation.element.DatePickerElement
import com.rustamft.tasksft.presentation.element.DropdownMenuElement
import com.rustamft.tasksft.presentation.element.IconButtonElement
import com.rustamft.tasksft.presentation.element.TextButtonElement
import com.rustamft.tasksft.presentation.element.TimePickerElement
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import com.rustamft.tasksft.presentation.theme.Shapes
import com.rustamft.tasksft.presentation.util.DEEP_LINK_URI
import com.rustamft.tasksft.presentation.util.ROUTE_EDITOR
import com.rustamft.tasksft.presentation.util.TAG_EDITOR_SCREEN
import com.rustamft.tasksft.presentation.util.TAG_EDITOR_SCREEN_EDITTEXT_TITLE
import com.rustamft.tasksft.presentation.util.TAG_EDITOR_SCREEN_FAB
import com.rustamft.tasksft.presentation.util.TASK_ID
import com.rustamft.tasksft.presentation.util.model.TaskStateHolder
import com.rustamft.tasksft.presentation.util.toDateTime
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination(
    route = ROUTE_EDITOR,
    deepLinks = [DeepLink(uriPattern = "$DEEP_LINK_URI$FULL_ROUTE_PLACEHOLDER")]
)
@Composable
fun EditorScreen(
    navigator: DestinationsNavigator, // From ComposeDestinations
    scaffoldState: ScaffoldState, // From DependenciesContainer
    taskId: Int?,
    viewModel: EditorViewModel = koinViewModel(
        parameters = { parametersOf(bundleOf(Pair(TASK_ID, taskId))) }
    )
) {

    LaunchedEffect(key1 = viewModel) {
        viewModel.successFlow.collect { success ->
            if (success) {
                navigator.popBackStack()
            }
        }
    }

    EditorScreenContent(
        scaffoldState = scaffoldState,
        taskStateHolderState = remember { mutableStateOf(viewModel.taskStateHolder) },
        openTaskInfoDialogState = viewModel.openTaskInfoDialogState,
        openChooseColorDialogState = viewModel.openChooseColorDialogState,
        openUnsavedTaskDialogState = viewModel.openUnsavedTaskDialogState,
        valueChangedState = viewModel.valueChangedState,
        onNavigateBack = { navigator.popBackStack() },
        onSaveTask = { viewModel.saveTask() },
        onDeleteTask = { viewModel.deleteTask() }
    )
}

@Composable
private fun EditorScreenContent(
    scaffoldState: ScaffoldState,
    taskStateHolderState: State<TaskStateHolder>,
    openTaskInfoDialogState: MutableState<Boolean>,
    openChooseColorDialogState: MutableState<Boolean>,
    openUnsavedTaskDialogState: MutableState<Boolean>,
    valueChangedState: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onSaveTask: () -> Unit,
    onDeleteTask: () -> Unit
) {

    val taskStateHolder by taskStateHolderState
    val onValueChange = {
        if (taskStateHolder.title.isBlank()) {
            valueChangedState.value = false
        } else if (!valueChangedState.value) {
            valueChangedState.value = true
        }
    }
    val pickerDialogThemeResId = if (AppTheme.isDark) {
        R.style.DateTimePickerDarkTheme
    } else {
        R.style.DateTimePickerLightTheme
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TAG_EDITOR_SCREEN),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                backButton = {
                    IconButtonElement(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.action_back),
                        onClick = {
                            if (valueChangedState.value) {
                                openUnsavedTaskDialogState.value = true
                            } else {
                                onNavigateBack()
                            }
                        }
                    )
                },
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_delete,
                        descriptionResId = R.string.action_delete,
                        onClick = onDeleteTask
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.task_info,
                        onClick = { openTaskInfoDialogState.value = true }
                    )
                )
            )
        },
        floatingActionButton = {
            Fab(
                modifier = Modifier.testTag(TAG_EDITOR_SCREEN_FAB),
                item = NavItem(
                    painterResId = R.drawable.ic_save,
                    descriptionResId = R.string.action_save,
                    onClick = onSaveTask
                ),
                visibilityState = valueChangedState
            )
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {

            val modifier = Modifier.padding(DIMEN_SMALL)
            val textFieldShape = Shapes.large
            val textFieldColors = TextFieldDefaults.textFieldColors(
                backgroundColor = taskStateHolder.color,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
            val switchColors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.primary,
                checkedTrackColor = AppTheme.colors.primary.copy(alpha = 0.3f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.3f)
            )

            TextField(
                modifier = modifier.testTag(TAG_EDITOR_SCREEN_EDITTEXT_TITLE),
                value = taskStateHolder.title,
                onValueChange = {
                    taskStateHolder.title = it
                    onValueChange()
                },
                placeholder = { Text(text = stringResource(id = R.string.task_title)) },
                shape = textFieldShape,
                colors = textFieldColors
            )
            TextField(
                modifier = modifier,
                value = taskStateHolder.description,
                onValueChange = {
                    taskStateHolder.description = it
                    onValueChange()
                },
                placeholder = { Text(text = stringResource(id = R.string.task_description)) },
                shape = textFieldShape,
                colors = textFieldColors
            )
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.task_color))
                Spacer(modifier = Modifier.width(DIMEN_SMALL))
                ColorButtonElement(
                    color = taskStateHolder.color,
                    onClick = { openChooseColorDialogState.value = true }
                )
            }
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.reminder))
                Spacer(modifier = Modifier.width(DIMEN_SMALL))
                Switch(
                    checked = taskStateHolder.reminderIsSet,
                    onCheckedChange = {
                        taskStateHolder.reminderIsSet = it
                        onValueChange()
                    },
                    colors = switchColors
                )
            }
            if (taskStateHolder.reminderIsSet) {
                Row(modifier = modifier) {
                    DatePickerElement(
                        calendarState = remember { mutableStateOf(taskStateHolder.reminderCalendar) },
                        themeResId = pickerDialogThemeResId,
                        onValueChange = onValueChange
                    )
                    Spacer(modifier = Modifier.width(DIMEN_SMALL))
                    TimePickerElement(
                        calendarState = remember { mutableStateOf(taskStateHolder.reminderCalendar) },
                        themeResId = pickerDialogThemeResId,
                        onValueChange = onValueChange
                    )
                    Spacer(modifier = Modifier.width(DIMEN_SMALL))
                    DropdownMenuElement(
                        items = TaskStateHolder.CALENDAR_UNITS_MAP,
                        selectedItemState = taskStateHolder.repeatCalendarUnitsState,
                        onClickAdditional = onValueChange
                    )
                }
            }
        }

        if (openTaskInfoDialogState.value) {
            AlertDialog(
                onDismissRequest = { openTaskInfoDialogState.value = false },
                title = { Text(text = stringResource(id = R.string.task_info)) },
                text = {
                    val createdString = if (taskStateHolder.created == 0L) {
                        stringResource(id = R.string.now)
                    } else {
                        val dateTime = taskStateHolder.created.toDateTime()
                        "${dateTime.date} ${dateTime.time}"
                    }
                    Text(
                        text = stringResource(
                            id = R.string.task_info_dialog_content,
                            createdString
                        )
                    )
                },
                confirmButton = {
                    TextButtonElement(
                        onClick = { openTaskInfoDialogState.value = false },
                        text = stringResource(R.string.action_close)
                    )
                },
                backgroundColor = MaterialTheme.colors.surface
            )
        }

        if (openChooseColorDialogState.value) {
            AlertDialog(
                onDismissRequest = { openChooseColorDialogState.value = false },
                title = { Text(text = stringResource(id = R.string.task_color)) },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppTheme.taskColors.forEach { color ->
                            ColorButtonElement(
                                color = color,
                                onClick = {
                                    taskStateHolder.color = color
                                    onValueChange()
                                    openChooseColorDialogState.value = false
                                }
                            )
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {},
                backgroundColor = MaterialTheme.colors.surface
            )
        }

        if (openUnsavedTaskDialogState.value) {
            AlertDialog(
                onDismissRequest = { openUnsavedTaskDialogState.value = false },
                title = { Text(text = stringResource(id = R.string.task_unsaved)) },
                text = { Text(text = stringResource(id = R.string.task_unsaved_dialog_content)) },
                confirmButton = {
                    TextButtonElement(
                        onClick = {
                            openUnsavedTaskDialogState.value = false
                            onSaveTask()
                        },
                        text = stringResource(R.string.action_save)
                    )
                },
                dismissButton = {
                    TextButtonElement(
                        onClick = {
                            openUnsavedTaskDialogState.value = false
                            onNavigateBack()
                        },
                        text = stringResource(R.string.action_discard)
                    )
                },
                backgroundColor = MaterialTheme.colors.surface
            )
        }
    }
}

@Preview
@Composable
private fun EditorScreenPreview() {
    AppTheme(theme = Theme.Auto) {
        EditorScreenContent(
            scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState()),
            taskStateHolderState = remember {
                mutableStateOf(
                    TaskStateHolder(
                        reminderIsSetState = mutableStateOf(true),
                        colorState = mutableStateOf(AppTheme.taskColors[0])
                    )
                )
            },
            openTaskInfoDialogState = remember { mutableStateOf(false) },
            openChooseColorDialogState = remember { mutableStateOf(false) },
            openUnsavedTaskDialogState = remember { mutableStateOf(false) },
            valueChangedState = remember { mutableStateOf(false) },
            onNavigateBack = {},
            onSaveTask = {},
            onDeleteTask = {}
        )
    }
}
