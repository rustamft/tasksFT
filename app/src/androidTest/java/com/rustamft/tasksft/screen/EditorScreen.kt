package com.rustamft.tasksft.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.rustamft.tasksft.R
import com.rustamft.tasksft.presentation.util.TAG_EDITOR_SCREEN
import com.rustamft.tasksft.presentation.util.TAG_EDITOR_SCREEN_EDITTEXT_TITLE
import com.rustamft.tasksft.presentation.util.TAG_EDITOR_SCREEN_FAB
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class EditorScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<EditorScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag(TAG_EDITOR_SCREEN) }
    ) {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    val deleteTopBarAction = child<KNode> {
        hasContentDescription(this@EditorScreen.context.getString(R.string.action_delete))
    }
    val saveFab = child<KNode> {
        hasTestTag(TAG_EDITOR_SCREEN_FAB)
    }
    val titleEditText = child<KNode> {
        hasTestTag(TAG_EDITOR_SCREEN_EDITTEXT_TITLE)
    }
}
