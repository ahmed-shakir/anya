package se.supernovait.anya.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import se.supernovait.anya.app.data.local.CatDatabase
import se.supernovait.anya.app.data.local.dao.CatDao
import se.supernovait.anya.app.data.local.dao.MedicalRecordDao
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.app.data.remote.InsultCensorApi
import se.supernovait.anya.app.data.repository.AuthRepositoryImpl
import se.supernovait.anya.app.data.repository.CatRepositoryImpl
import se.supernovait.anya.app.data.repository.InsultCensorRepositoryImpl
import se.supernovait.anya.app.domain.navigation.DeepLinkHandler
import se.supernovait.anya.app.domain.navigation.DeepLinkHandlerImpl
import se.supernovait.anya.app.domain.repository.AuthRepository
import se.supernovait.anya.app.domain.repository.CatRepository
import se.supernovait.anya.app.domain.repository.InsultCensorRepository
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.app.initialization.AppInitializer
import se.supernovait.anya.app.presentation.cat.CatViewModel
import se.supernovait.anya.app.presentation.censored_text.CensoredTextViewModel
import se.supernovait.anya.app.presentation.import.ImportViewModel
import se.supernovait.anya.app.presentation.medical_record.MedicalRecordViewModel
import se.supernovait.anya.app.presentation.owner.OwnerViewModel
import se.supernovait.anya.app.presentation.welcome.WelcomeViewModel
import se.supernovait.anya.core.data.database.DatabaseFactory

expect val platformModule: Module

val sharedModule = module {
    singleOf(::AppInitializer)
    singleOf(::AuthenticationManager)
    singleOf(::DeepLinkHandlerImpl).bind<DeepLinkHandler>()

    singleOf(::InsultCensorApi)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::CatRepositoryImpl).bind<CatRepository>()
    singleOf(::InsultCensorRepositoryImpl).bind<InsultCensorRepository>()

    viewModelOf(::CensoredTextViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::CatViewModel)
    viewModelOf(::OwnerViewModel)
    viewModelOf(::MedicalRecordViewModel)
    viewModelOf(::ImportViewModel)

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single<CatDatabase> {
        DatabaseFactory.create(get())
    }

    single<CatDao> {
        get<CatDatabase>().catDao()
    }

    single<OwnerDao> {
        get<CatDatabase>().ownerDao()
    }

    single<MedicalRecordDao> {
        get<CatDatabase>().medicalRecordDao()
    }
}
