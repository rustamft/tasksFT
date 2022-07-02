package com.rustamft.tasksft.presentation.screen.backup

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.rustamft.tasksft.domain.util.ROUTE_BACKUP
import com.rustamft.tasksft.presentation.element.IconButtonElement
import com.rustamft.tasksft.presentation.navigation.TopBar
import org.koin.androidx.compose.koinViewModel

@Destination(route = ROUTE_BACKUP)
@Composable
fun BackupScreen(
    navigator: DestinationsNavigator, // From ComposeDestinations
    scaffoldState: ScaffoldState, // From DependenciesContainer
    viewModel: BackupViewModel = koinViewModel(),
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
                        onClick = { navigator.popBackStack() }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (appPreferences.backupDirectory == "") {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                        exportLauncher.launch(intent)
                    } else {
                        // TODO: show dialog
                    }
                },
                content = {
                    Text(text = stringResource(id = R.string.backup_export))
                }
            )
            Button(
                onClick = {
                    if (appPreferences.backupDirectory == "") {
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/json"
                        }
                        importLauncher.launch(intent)
                    } else {
                        // TODO: show dialog
                    }
                },
                content = {
                    Text(text = stringResource(id = R.string.backup_import))
                }
            )
        }
    }
}
