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
import androidx.compose.material.Button
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.util.ROUTE_EDITOR
import com.rustamft.tasksft.domain.util.format
import com.rustamft.tasksft.presentation.navigation.Fab
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Destination(
    route = ROUTE_EDITOR,
    navArgsDelegate = EditorScreenNavArgs::class
)
@Composable
fun EditorScreen(
    context: Context = LocalContext.current,
    viewModel: EditorViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    scaffoldState: ScaffoldState // From DependenciesContainer.
) {

    var fabVisible by remember { mutableStateOf(false) }
    val onValueChange = {
        if (!fabVisible) { fabVisible = true }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.errorFlow.collect { error ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = error
            )
        }
    }

    @Composable
    fun DatePickerElement() {
        with(viewModel.mutableTask.reminderCalendar) {
            val formatter = SimpleDateFormat("MMM", Locale.getDefault())
            var text by remember {
                mutableStateOf(
                    "${
                        get(Calendar.DAY_OF_MONTH)
                    } ${
                        formatter.format(get(Calendar.MONTH))
                    } ${
                        get(Calendar.YEAR)
                    }"
                )
            }
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year: Int, month: Int, day: Int ->
                    text = "$day ${formatter.format(month)} $year"
                    set(year, month, day)
                }, get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH)
            )
            Button(onClick = {
                datePickerDialog.show()
            }) {
                Text(text = text)
            }
        }
    }

    @Composable
    fun TimePickerElement() {
        with(viewModel.mutableTask.reminderCalendar) {
            var text by remember {
                mutableStateOf(
                    "${
                        get(Calendar.HOUR_OF_DAY).format(2)
                    }:${
                        get(Calendar.MINUTE).format(2)
                    }"
                )
            }
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    text = "${hour.format(2)}:${minute.format(2)}"
                    apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }
                },
                get(Calendar.HOUR_OF_DAY),
                get(Calendar.MINUTE),
                true
            )
            Button(onClick = {
                timePickerDialog.show()
            }) {
                Text(text = text)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navigator = navigator,
                hasBackButton = true,
                items = listOf(
                    NavItem(
                        painterResId = R.drawable.ic_info,
                        descriptionResId = R.string.task_info,
                        onClick = { /* TODO: implement task info */ }
                    )
                )
            )
        },
        floatingActionButton = {
            if (fabVisible) {
                Fab(
                    item = NavItem(
                        painterResId = R.drawable.ic_save,
                        descriptionResId = R.string.action_save,
                        onClick = {
                            viewModel.saveTask() // TODO: check if reminder is needed
                            navigator.popBackStack()
                        }
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
    }
}
