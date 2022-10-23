package com.rustamft.tasksft.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.rustamft.tasksft.presentation.util.TAG_LIST_SCREEN
import com.rustamft.tasksft.presentation.util.TAG_LIST_SCREEN_FAB
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ListScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ListScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag(TAG_LIST_SCREEN) }
    ) {

    val addFab: KNode = child {
        hasTestTag(TAG_LIST_SCREEN_FAB)
    }
}
