package se.supernovait.anya.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import se.supernovait.anya.app.presentation.app.initialization.AppInitializer

class KoinHelper : KoinComponent {
    private val appInitializer: AppInitializer by inject()

    fun getAppInitializer(): AppInitializer = appInitializer
}
