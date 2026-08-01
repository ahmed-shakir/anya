package se.supernovait.anya.app.di

import androidx.test.core.app.ApplicationProvider
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import se.supernovait.anya.app.domain.navigation.DeepLinkHandler
import se.supernovait.anya.app.domain.repository.AuthRepository
import se.supernovait.anya.app.domain.repository.CatRepository
import se.supernovait.anya.app.fakes.FakeAuthRepository
import se.supernovait.anya.app.fakes.FakeCatRepository
import se.supernovait.anya.app.fakes.FakeDeepLinkHandler
import se.supernovait.anya.app.fakes.FakeDeviceManager
import se.supernovait.anya.app.fakes.FakeNetworkHandler
import se.supernovait.anya.app.fakes.FakeShareHandler
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.cat.CatViewModel
import se.supernovait.anya.app.presentation.import.ImportViewModel
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordViewModel
import se.supernovait.anya.app.presentation.owner.OwnerViewModel
import se.supernovait.anya.app.presentation.welcome.WelcomeViewModel
import se.supernovait.anya.core.domain.file.PdfViewer
import se.supernovait.anya.core.domain.manager.DeviceManager
import se.supernovait.anya.core.domain.network.NetworkHandler
import se.supernovait.anya.core.domain.sharing.ShareHandler

actual fun getTestModule() = module {
    single { Json { ignoreUnknownKeys = true; coerceInputValues = true } }
    single<AuthRepository> { FakeAuthRepository() }
    single<CatRepository> { FakeCatRepository() }
    single<NetworkHandler> { FakeNetworkHandler() }
    single<DeviceManager> { FakeDeviceManager() }
    single<DeepLinkHandler> { FakeDeepLinkHandler() }
    single<ShareHandler> { FakeShareHandler() }
    single<PdfViewer> { PdfViewer(ApplicationProvider.getApplicationContext()) }
    single { AuthenticationManager(get()) }
    single { Json { ignoreUnknownKeys = true; coerceInputValues = true } }

    viewModelOf(::WelcomeViewModel)
    viewModelOf(::CatViewModel)
    viewModelOf(::OwnerViewModel)
    viewModelOf(::MedicalRecordViewModel)
    viewModelOf(::ImportViewModel)
}
