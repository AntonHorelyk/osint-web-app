package com.example.osint.di

import org.koin.dsl.module
import com.example.osint.repository.ScanRepository
import com.example.osint.repository.ScanRepositoryImpl
import com.example.osint.core.spi.ToolAdapter
import com.example.osint.adapters.AmassAdapter
import com.example.osint.adapters.HarvesterAdapter
import com.example.osint.service.ScanService

val appModule = module {
    single<ScanRepository> { ScanRepositoryImpl() }
    single<List<ToolAdapter>> { listOf(AmassAdapter(), HarvesterAdapter()) }
    single { ScanService(get(), get()) }
}
