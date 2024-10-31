package com.kenkoro.cryptoTracker.di

import com.kenkoro.cryptoTracker.core.data.networking.HttpClientFactory
import com.kenkoro.cryptoTracker.crypto.data.networking.RemoteCoinDataSource
import com.kenkoro.cryptoTracker.crypto.domain.CoinDataSource
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule =
  module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()
    viewModelOf(::CoinListViewModel)
  }