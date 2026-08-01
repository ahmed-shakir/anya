package se.supernovait.anya.app.domain.navigation

import kotlinx.coroutines.flow.SharedFlow
import se.supernovait.anya.app.presentation.navigation.Route

interface DeepLinkHandler {
    val events: SharedFlow<Route>
    fun handleDeepLink(url: String)
}
