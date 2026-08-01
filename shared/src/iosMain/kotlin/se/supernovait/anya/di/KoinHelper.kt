package se.supernovait.anya.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import se.supernovait.anya.app.domain.navigation.DeepLinkHandler
import se.supernovait.anya.app.presentation.app.initialization.AppInitializer

class KoinHelper : KoinComponent {
    private val appInitializer: AppInitializer by inject()
    private val deepLinkHandler: DeepLinkHandler by inject()

    fun getAppInitializer(): AppInitializer = appInitializer

    fun getDeepLinkHandler(): DeepLinkHandler = deepLinkHandler
}
