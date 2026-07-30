package se.supernovait.anya.app.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import se.supernovait.anya.app.domain.repository.AuthRepository
import se.supernovait.anya.app.fakes.FakeAuthRepository
import se.supernovait.anya.app.fakes.FakeDeviceManager
import se.supernovait.anya.app.fakes.FakeNetworkHandler
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.network.NetworkHandler

actual fun getTestModule() = module {
    single { Json { ignoreUnknownKeys = true; coerceInputValues = true } }
    single<AuthRepository> { FakeAuthRepository() }
    single<NetworkHandler> { FakeNetworkHandler() }
    single<DeviceManager> { FakeDeviceManager() }
    single { AuthenticationManager(get()) }
}
