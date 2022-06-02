package com.rustamft.tasksft.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.rememberScaffoldState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.rustamft.tasksft.presentation.screen.NavGraphs
import com.rustamft.tasksft.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val scaffoldState = rememberScaffoldState()
            val navController = rememberNavController()

            AppTheme {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = navController,
                    dependenciesContainerBuilder = {
                        dependency(scaffoldState)
                    }
                )
            }
        }
    }
}
