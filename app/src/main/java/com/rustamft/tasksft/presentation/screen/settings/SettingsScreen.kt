package com.rustamft.tasksft.presentation.screen.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.model.Preferences.Theme
import com.rustamft.tasksft.presentation.element.IconButtonElement
import com.rustamft.tasksft.presentation.element.TextButtonElement
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import com.rustamft.tasksft.presentation.util.ROUTE_SETTINGS
import org.koin.androidx.compose.koinViewModel

@Destination(route = ROUTE_SETTINGS)
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator, // From ComposeDestinations
    scaffoldState: ScaffoldState, // From DependenciesContainer
    viewModel: SettingsViewModel = koinViewModel()
) {

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.exportTasks(directoryUri = uri)
            }
        }
    }
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.importTasks(fileUri = uri)
            }
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.successFlow.collect { success ->
            if (success) {
                navigator.popBackStack()
            }
        }
    }

    SettingsScreenContent(
        scaffoldState = scaffoldState,
        preferencesState = viewModel.preferencesFlow.collectAsState(initial = Preferences()),
        openExportConfirmDialogState = viewModel.openExportConfirmDialogState,
        onNavigateBack = { navigator.popBackStack() },
        onSetTheme = { theme -> viewModel.setTheme(theme) },
        onChooseDirectory = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            exportLauncher.launch(intent)
        },
        onChooseFile = {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/*"
            } // for old API works type "application/octet-stream", for new - "application/json"
            importLauncher.launch(intent)
        },
        onExportTasks = { uri -> viewModel.exportTasks(uri) }
    )
}

@Composable
private fun SettingsScreenContent(
    scaffoldState: ScaffoldState,
    preferencesState: State<Preferences>,
    openExportConfirmDialogState: MutableState<Boolean>,
    onNavigateBack: () -> Unit,
    onSetTheme: (Theme) -> Unit,
    onChooseDirectory: () -> Unit,
    onChooseFile: () -> Unit,
    onExportTasks: (Uri) -> Unit
) {

    val preferences by preferencesState

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                items = emptyList(),
                backButton = {
                    IconButtonElement(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.action_back),
                        onClick = onNavigateBack
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                val spacerModifier = Modifier.width(DIMEN_SMALL)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.theme))
                    Spacer(modifier = spacerModifier)

                    val painterId: Int
                    val contentDescriptionId: Int
                    val onThemeSwitch = when (preferences.theme) {
                        is Theme.Auto -> {
                            painterId = R.drawable.ic_theme_auto
                            contentDescriptionId = R.string.theme_auto
                            { onSetTheme(Theme.Light) }
                        }
                        is Theme.Light -> {
                            painterId = R.drawable.ic_theme_light
                            contentDescriptionId = R.string.theme_light
                            { onSetTheme(Theme.Dark) }
                        }
                        is Theme.Dark -> {
                            painterId = R.drawable.ic_theme_dark
                            contentDescriptionId = R.string.theme_dark
                            { onSetTheme(Theme.Auto) }
                        }
                    }

                    IconButtonElement(
                        painter = painterResource(id = painterId),
                        contentDescription = stringResource(id = contentDescriptionId),
                        onClick = onThemeSwitch
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.backup))
                    Spacer(modifier = spacerModifier)
                    IconButtonElement(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = stringResource(id = R.string.action_save),
                        onClick = {
                            if (preferences.backupDirectory.isEmpty()) {
                                onChooseDirectory()
                            } else {
                                openExportConfirmDialogState.value = true
                            }
                        }
                    )
                    Spacer(modifier = spacerModifier)
                    IconButtonElement(
                        painter = painterResource(id = R.drawable.ic_restore),
                        contentDescription = stringResource(id = R.string.action_restore),
                        onClick = onChooseFile
                    )
                }
            }
        }
    }

    if (openExportConfirmDialogState.value) {
        AlertDialog(
            onDismissRequest = { openExportConfirmDialogState.value = false },
            title = { Text(text = stringResource(id = R.string.backup)) },
            text = {
                Text(
                    text = stringResource(
                        id = R.string.backup_dialog_content,
                        preferences.backupDirectory
                    )
                )
            },
            confirmButton = {
                TextButtonElement(
                    onClick = onChooseDirectory,
                    text = stringResource(id = R.string.backup_dialog_choose_dir)
                )
            },
            dismissButton = {
                TextButtonElement(
                    onClick = { onExportTasks(preferences.backupDirectory.toUri()) },
                    text = stringResource(R.string.action_save)
                )
            },
            backgroundColor = MaterialTheme.colors.surface
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreenContent(
        scaffoldState = ScaffoldState(DrawerState(DrawerValue.Open), SnackbarHostState()),
        preferencesState = remember { mutableStateOf(Preferences()) },
        openExportConfirmDialogState = remember { mutableStateOf(false) },
        onNavigateBack = {},
        onSetTheme = {},
        onChooseDirectory = {},
        onChooseFile = {},
        onExportTasks = {}
    )
}
