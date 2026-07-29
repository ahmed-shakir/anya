package se.supernovait.anya.app.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module

actual fun getTestModule() = module {
    single { Json { ignoreUnknownKeys = true; coerceInputValues = true } }
}
