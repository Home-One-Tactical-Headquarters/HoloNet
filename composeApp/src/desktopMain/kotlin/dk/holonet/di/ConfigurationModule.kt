package dk.holonet.di

import dk.holonet.configuration.ConfigurationService
import org.koin.dsl.module

val configurationModule = module {
    single {
        ConfigurationService()
    }
}