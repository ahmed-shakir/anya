package se.supernovait.anya.app.presentation.app

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import se.supernovait.anya.app.util.AnyaBaseTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AnyaAppNavigationTest : AnyaBaseTest() {

    @Test
    fun `when app starts and user is not authenticated it shows welcome screen`() = runComposeUiTest {
        setContent {
            AnyaApp()
        }

        // Welcome screen has a "Sign up" button
        onNodeWithText("Sign up", substring = true, ignoreCase = true).assertExists()
    }

}
