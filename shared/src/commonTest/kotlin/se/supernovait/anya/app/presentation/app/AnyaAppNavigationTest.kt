package se.supernovait.anya.app.presentation.app

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import se.supernovait.anya.app.data.local.entity.Owner
import se.supernovait.anya.app.fakes.FakeAuthRepository
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

    @Test
    fun `when app starts and user is authenticated it shows start screen`() = runComposeUiTest {
        val koin = org.koin.mp.KoinPlatform.getKoin()
        val repo = koin.get<se.supernovait.anya.app.domain.repository.AuthRepository>() as FakeAuthRepository

        repo.signUp(Owner(id = 1, firstname = "John", lastname = "Doe", username = "johndoe", dob = "1990-01-01"))
        repo.signIn("johndoe")

        advanceTime()

        setContent {
            AnyaApp()
        }

        onNodeWithText("Cats", ignoreCase = true).assertExists()
        onNodeWithText("Owners", ignoreCase = true).assertExists()
    }
}
