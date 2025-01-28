package se.supernovait.anya.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import se.supernovait.anya.core.domain.util.DeviceManager

actual val platformModule = module {
    singleOf(::DeviceManager)
}
