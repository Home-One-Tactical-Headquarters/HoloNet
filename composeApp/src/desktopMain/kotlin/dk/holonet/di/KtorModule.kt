package dk.holonet.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

val ktorModule = module {
    single<HttpClient> {
        HttpClient(CIO)
    }
}