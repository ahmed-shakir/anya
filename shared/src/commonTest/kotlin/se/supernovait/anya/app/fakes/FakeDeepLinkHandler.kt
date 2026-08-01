package se.supernovait.anya.app.fakes

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import se.supernovait.anya.app.domain.navigation.DeepLinkHandler
import se.supernovait.anya.app.presentation.navigation.Route

class FakeDeepLinkHandler : DeepLinkHandler {
    override val events: SharedFlow<Route> = MutableSharedFlow()
    override fun handleDeepLink(url: String) {}
}
