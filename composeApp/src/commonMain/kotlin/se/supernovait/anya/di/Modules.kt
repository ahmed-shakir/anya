package se.supernovait.anya.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import se.supernovait.anya.app.data.network.RemoteInsultCensorDataSource
import se.supernovait.anya.app.domain.InsultCensorDataSource
import se.supernovait.anya.app.presentation.censored_text.CensoredTextViewModel

expect val platformModule: Module

val sharedModule = module {
    singleOf(::RemoteInsultCensorDataSource).bind<InsultCensorDataSource>()

    viewModelOf(::CensoredTextViewModel)
}
