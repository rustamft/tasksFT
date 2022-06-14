package com.rustamft.tasksft.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.rustamft.tasksft.presentation.navigation.FAB
import com.rustamft.tasksft.presentation.navigation.NavItem
import com.rustamft.tasksft.presentation.navigation.TopBar
import com.rustamft.tasksft.presentation.screen.NavGraphs
import com.rustamft.tasksft.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()
            val topBarItemsFlow = MutableStateFlow(emptyList<NavItem>())
            val topBarItems by topBarItemsFlow.collectAsState() // TODO: fix items missing in bar
            val fabItemFlow = MutableStateFlow<NavItem?>(null)
            val fabItem by fabItemFlow.collectAsState()

            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState,
                    topBar = { TopBar(navController = navController, items = topBarItems) },
                    floatingActionButton = {
                        if (fabItem != null) {
                            FAB(item = fabItem!!)
                        }
                    }
                ) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController,
                        dependenciesContainerBuilder = {
                            dependency(it)
                            dependency(scaffoldState)
                            dependency(topBarItemsFlow)
                            dependency(fabItemFlow)
                        }
                    )
                }
            }
        }
    }
}
