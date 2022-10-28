package com.rustamft.tasksft.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.util.TAG_LIST_SCREEN
import com.rustamft.tasksft.presentation.util.TAG_LIST_SCREEN_FAB
import com.rustamft.tasksft.presentation.util.TAG_LIST_SCREEN_TASK_CARD
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class ListScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ListScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag(TAG_LIST_SCREEN) }
    ) {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    val cleanTopBarAction = child<KNode> {
        hasContentDescription(this@ListScreen.context.getString(R.string.action_delete_finished))
    }
    val addFab = child<KNode> {
        hasTestTag(TAG_LIST_SCREEN_FAB)
    }
    val taskCard = child<KNode> {
        hasTestTag(TAG_LIST_SCREEN_TASK_CARD)
    }
}
