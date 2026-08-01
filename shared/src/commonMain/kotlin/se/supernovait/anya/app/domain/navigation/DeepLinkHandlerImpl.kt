package se.supernovait.anya.app.domain.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import se.supernovait.anya.app.presentation.navigation.Route
import se.supernovait.anya.core.domain.sharing.ShareUrlBuilder

class DeepLinkHandlerImpl : DeepLinkHandler {
    private val _events = MutableSharedFlow<Route>(extraBufferCapacity = 1)
    override val events: SharedFlow<Route> = _events.asSharedFlow()

    override fun handleDeepLink(url: String) {
        val (type, data) = ShareUrlBuilder.parse(url)

        if (!type.isNullOrBlank() && !data.isNullOrBlank()) {
            val route = Route.Import(type, data)
            _events.tryEmit(route)
        }
    }
}
