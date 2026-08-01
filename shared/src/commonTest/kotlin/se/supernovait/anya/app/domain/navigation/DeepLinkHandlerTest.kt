package se.supernovait.anya.app.domain.navigation

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DeepLinkHandlerTest {
    private lateinit var deepLinkHandler: DeepLinkHandler

    @BeforeTest
    fun setup() {
        deepLinkHandler = DeepLinkHandlerImpl()
    }

    @Test
    fun `when invalid deep link is handled it emits nothing`() = runTest {
        val url = "https://invalid.com/share?type=cat&data=123"

        deepLinkHandler.events.test {
            deepLinkHandler.handleDeepLink(url)
            expectNoEvents()
        }
    }
}
