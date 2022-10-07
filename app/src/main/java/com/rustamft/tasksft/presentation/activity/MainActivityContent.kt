package com.rustamft.tasksft.presentation.activity

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.rustamft.tasksft.domain.model.AppPreferences
import com.rustamft.tasksft.presentation.screen.NavGraphs
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.util.UIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.compose.get
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainActivityContent(
    context: Context = LocalContext.current,
    snackbarChannel: Channel<UIText> = get(),
    snackbarFlow: Flow<UIText> = snackbarChannel.receiveAsFlow(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = koinViewModel()
) {

    val appPreferences by viewModel.appPreferencesFlow.collectAsState(
        initial = AppPreferences()
    )

    LaunchedEffect(Unit) {
        snackbarFlow.collect { uiText ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiText.asString(context)
            )
        }
    }

    AppTheme(darkTheme = appPreferences.darkTheme) {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController,
            dependenciesContainerBuilder = {
                dependency(scaffoldState)
            }
        )
    }
}
