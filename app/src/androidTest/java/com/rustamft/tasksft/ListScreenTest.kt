package com.rustamft.tasksft

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.components.composesupport.interceptors.behavior.impl.systemsafety.SystemDialogSafetySemanticsBehaviorInterceptor
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.params.FlakySafetyParams
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.rustamft.tasksft.presentation.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListScreenTest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport(
        customize = {
            flakySafetyParams = FlakySafetyParams.custom(timeoutMs = 5000, intervalMs = 1000)
        },
        lateComposeCustomize = { composeBuilder ->
            composeBuilder.semanticsBehaviorInterceptors =
                composeBuilder.semanticsBehaviorInterceptors.filter {
                    it !is SystemDialogSafetySemanticsBehaviorInterceptor
                }.toMutableList()
        }
    )
) {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun test() = run {
        step("Description") {
            // test
        }
    }
}
