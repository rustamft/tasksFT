package com.rustamft.tasksft.presentation.screen.settings

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rustamft.tasksft.R
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.domain.util.ROUTE_SETTINGS
import com.rustamft.tasksft.presentation.composable.IconButtonComposable
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.theme.DIMEN_SMALL
import org.koin.androidx.compose.koinViewModel

@Destination(route = ROUTE_SETTINGS)
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator, // From ComposeDestinations
    scaffoldState: ScaffoldState, // From DependenciesContainer
    viewModel: SettingsViewModel = koinViewModel(),
    appPreferencesState: State<AppPreferences> = viewModel.appPreferencesFlow.collectAsState(
        initial = AppPreferences()
    )
) {

    val appPreferences by appPreferencesState
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.exportTasks(directoryUri = result.data?.data)
        }
    }
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.importTasks(fileUri = result.data?.data)
        }
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.successFlow.collect { success ->
            if (success) {
                navigator.popBackStack()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                items = emptyList(),
                backButton = {
                    IconButtonComposable(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.action_back),
                        onClick = { navigator.popBackStack() }
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
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
                    val onThemeSwitch = if (appPreferences.darkTheme == null) {
                        painterId = R.drawable.ic_theme_auto
                        contentDescriptionId = R.string.theme_auto
                        { viewModel.setTheme(darkTheme = true) }
                    } else if (appPreferences.darkTheme!!) {
                        painterId = R.drawable.ic_theme_dark
                        contentDescriptionId = R.string.theme_dark
                        { viewModel.setTheme(darkTheme = false) }
                    } else {
                        painterId = R.drawable.ic_theme_light
                        contentDescriptionId = R.string.theme_light
                        { viewModel.setTheme(darkTheme = null) }
                    }

                    IconButtonComposable(
                        painter = painterResource(id = painterId),
                        contentDescription = stringResource(id = contentDescriptionId),
                        onClick = onThemeSwitch
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.backup))
                    Spacer(modifier = spacerModifier)
                    IconButtonComposable(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = stringResource(id = R.string.action_save),
                        onClick = {
                            if (appPreferences.backupDirectory == "") {
                                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                                exportLauncher.launch(intent)
                            } else {
                                // TODO: show dialog
                            }
                        }
                    )
                    Spacer(modifier = spacerModifier)
                    IconButtonComposable(
                        painter = painterResource(id = R.drawable.ic_restore),
                        contentDescription = stringResource(id = R.string.action_restore),
                        onClick = {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "application/json"
                            }
                            importLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}
