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
import com.rustamft.tasksft.domain.model.Preferences
import com.rustamft.tasksft.domain.usecase.GetPreferencesUseCase
import com.rustamft.tasksft.presentation.screen.NavGraphs
import com.rustamft.tasksft.presentation.theme.AppTheme
import com.rustamft.tasksft.presentation.util.model.UIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.androidx.compose.get

@Composable
fun MainActivityContent(
    context: Context = LocalContext.current,
    snackbarChannel: Channel<UIText> = get(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    getPreferencesUseCase: GetPreferencesUseCase = get()
) {

    val preferences by getPreferencesUseCase.execute().collectAsState(initial = Preferences())

    LaunchedEffect(Unit) {
        snackbarChannel.receiveAsFlow().collect { uiText ->
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiText.asString(context)
            )
        }
    }

    AppTheme(theme = preferences.theme) {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController,
            dependenciesContainerBuilder = {
                dependency(scaffoldState)
            }
        )
    }
}
