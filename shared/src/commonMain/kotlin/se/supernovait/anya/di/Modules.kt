package se.supernovait.anya.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import se.supernovait.anya.app.data.local.CatDatabase
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.app.data.repository.AuthRepositoryImpl
import se.supernovait.anya.app.domain.repository.AuthRepository
import se.supernovait.anya.app.presentation.app.auth.AuthenticationManager
import se.supernovait.anya.app.presentation.app.initialization.AppInitializer
import se.supernovait.anya.app.presentation.welcome.WelcomeViewModel
import se.supernovait.anya.core.data.database.DatabaseFactory

expect val platformModule: Module

val sharedModule = module {
    singleOf(::AppInitializer)
    singleOf(::AuthenticationManager)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

    viewModelOf(::WelcomeViewModel)

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single<CatDatabase> {
        DatabaseFactory.create(get())
    }

    single<OwnerDao> {
        get<CatDatabase>().ownerDao()
    }
}
