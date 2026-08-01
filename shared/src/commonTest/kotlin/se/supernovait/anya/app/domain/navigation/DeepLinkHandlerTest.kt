package se.supernovait.anya.app.domain.navigation

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.domain.sharing.ShareUrlBuilder
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeepLinkHandlerTest {
    private lateinit var deepLinkHandler: DeepLinkHandler

    @BeforeTest
    fun setup() {
        deepLinkHandler = DeepLinkHandlerImpl()
    }

    @Test
    fun `when valid deep link is handled it emits Import route`() = runTest {
        val type = "cat"
        val data = "{\"name\":\"Whiskers\"}"
        val url = ShareUrlBuilder.build(type, data)

        deepLinkHandler.events.test {
            deepLinkHandler.handleDeepLink(url)
            val event = awaitItem()
            assertTrue(event is Route.Import)
            assertEquals(type, (event as Route.Import).type)
            assertEquals(data, (event as Route.Import).data)
        }
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
