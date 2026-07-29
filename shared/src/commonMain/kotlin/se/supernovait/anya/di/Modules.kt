package se.supernovait.anya.di

import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import se.supernovait.anya.app.data.local.CatDatabase
import se.supernovait.anya.app.data.local.dao.OwnerDao
import se.supernovait.anya.core.data.database.DatabaseFactory

expect val platformModule: Module

val sharedModule = module {

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
