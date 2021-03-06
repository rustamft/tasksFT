package com.rustamft.tasksft.presentation.screen.editor

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.FULL_ROUTE_PLACEHOLDER
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.util.DEEP_LINK_URI
import com.rustamft.tasksft.domain.util.ROUTE_EDITOR
import com.rustamft.tasksft.domain.util.TASK_ID
import com.rustamft.tasksft.domain.util.format
import com.rustamft.tasksft.domain.util.toDateTime
import com.rustamft.tasksft.presentation.composable.IconButtonComposable
import com.rustamft.tasksft.presentation.composable.TextButtonComposable
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Calendar
import java.util.Locale

@Destination(
    route = ROUTE_EDITOR,
    deepLinks = [DeepLink(uriPattern = "${DEEP_LINK_URI}${FULL_ROUTE_PLACEHOLDER}")]
)
@Composable
fun EditorScreen(
    navigator: DestinationsNavigator, // From ComposeDestinations
    scaffoldState: ScaffoldState, // From DependenciesContainer
    context: Context = LocalContext.current,
    taskId: Int?,
    viewModel: EditorViewModel = koinViewModel(
        parameters = { parametersOf(bundleOf(Pair(TASK_ID, taskId))) }
    )
) {

    var openTaskInfoDialog by remember { mutableStateOf(false) }
    var openUnsavedTaskDialog by remember { mutableStateOf(false) }
    var valueChanged by remember { mutableStateOf(false) }
    val onValueChange = {
        if (viewModel.mutableTask.title.isBlank()) {
            valueChanged = false
        } else if (!valueChanged) {
            valueChanged = true
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.successFlow.collect { success ->
            if (success) {
                navigator.popBackStack()
            }
        }
    }

    @Composable
    fun DatePickerElement() {
        with(viewModel.mutableTask.reminderCalendar) {

            fun getStringFromCalendar(): String {
                return "${
                    get(Calendar.DAY_OF_MONTH)
                } ${
                    getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                } ${
                    get(Calendar.YEAR)
                }"
            }

            var text by remember { mutableStateOf(getStringFromCalendar()) }
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year: Int, month: Int, day: Int ->
                    set(year, month, day)
                    text = getStringFromCalendar()
                    onValueChange()
                }, get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)
            )
            Button(
                onClick = {
                    datePickerDialog.show()
                },
                content = {
                    Text(text = text)
                }
            )
        }
    }

    @Composable
    fun TimePickerElement() {
        with(viewModel.mutableTask.reminderCalendar) {

            fun getStringFromCalendar(): String {
                return "${
                    get(Calendar.HOUR_OF_DAY).format(2)
                }:${
                    get(Calendar.MINUTE).format(2)
                }"
            }

            var text by remember { mutableStateOf(getStringFromCalendar()) }
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                    text = getStringFromCalendar()
                    onValueChange()
                },
                get(Calendar.HOUR_OF_DAY),
                get(Calendar.MINUTE),
                true
            )
            Button(
                onClick = {
                    timePickerDialog.show()
                },
                content = {
                    Text(text = text)
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                backButton = {
                    IconButtonComposable(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.action_back),
                        onClick = {
                            if (valueChanged) {
                                openUnsavedTaskDialog = true
                            } else {
                                navigator.popBackStack()
                            }
                        }
                    )
                },
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_delete,
                        descriptionResId = R.string.action_delete,
                        onClick = { viewModel.deleteTask() }
                    ),
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.task_info,
                        onClick = { openTaskInfoDialog = true }
                    )
                )
            )
        },
        floatingActionButton = {
            if (valueChanged) {
                Fab(
                    item = NavItem(
                        painterResId = R.drawable.ic_save,
                        descriptionResId = R.string.action_save,
                        onClick = { viewModel.saveTask() }
                    )
                )
            }
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {

            val modifier = Modifier
                .padding(DIMEN_SMALL)

            TextField(
                modifier = modifier,
                value = viewModel.mutableTask.title,
                onValueChange = {
                    viewModel.mutableTask.title = it
                    onValueChange()
                },
                placeholder = { Text(text = stringResource(id = R.string.title)) }
            )
            TextField(
                modifier = modifier,
                value = viewModel.mutableTask.description,
                onValueChange = {
                    viewModel.mutableTask.description = it
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
                    checked = viewModel.mutableTask.reminderIsSet,
                    onCheckedChange = {
                        viewModel.mutableTask.reminderIsSet = it
                        onValueChange()
                    }
                )
            }
            if (viewModel.mutableTask.reminderIsSet) {
                Row(modifier = modifier) {
                    DatePickerElement()
                    Spacer(modifier = Modifier.width(DIMEN_SMALL))
                    TimePickerElement()
                }
            }
        }

        if (openTaskInfoDialog) {
            AlertDialog(
                onDismissRequest = { openTaskInfoDialog = false },
                title = { Text(text = stringResource(id = R.string.task_info)) },
                text = {
                    val createdString = if (viewModel.mutableTask.created == 0L) {
                        stringResource(id = R.string.now)
                    } else {
                        val dateTime = viewModel.mutableTask.created.toDateTime()
                        "${dateTime.date} ${dateTime.time}"
                    }
                    Text(text = stringResource(id = R.string.task_info_dialog_content, createdString))
                },
                confirmButton = {
                    TextButtonComposable(
                        onClick = { openTaskInfoDialog = false },
                        text = stringResource(R.string.action_close)
                    )
                },
                backgroundColor = MaterialTheme.colors.surface
            )
        }

        if (openUnsavedTaskDialog) {
            AlertDialog(
                onDismissRequest = { openUnsavedTaskDialog = false },
                title = { Text(text = stringResource(id = R.string.task_unsaved)) },
                text = { Text(text = stringResource(id = R.string.task_unsaved_dialog_content)) },
                confirmButton = {
                    TextButtonComposable(
                        onClick = {
                            openUnsavedTaskDialog = false
                            navigator.popBackStack()
                        },
                        text = stringResource(R.string.action_yes)
                    )
                },
                dismissButton = {
                    TextButtonComposable(
                        onClick = { openUnsavedTaskDialog = false },
                        text = stringResource(R.string.action_no)
                    )
                },
                backgroundColor = MaterialTheme.colors.surface
            )
        }
    }
}
