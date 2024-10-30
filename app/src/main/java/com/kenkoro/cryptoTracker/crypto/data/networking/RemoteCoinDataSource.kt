package com.kenkoro.cryptoTracker.crypto.data.networking

import com.kenkoro.cryptoTracker.core.data.networking.constructUrl
import com.kenkoro.cryptoTracker.core.data.networking.safeCall
import com.kenkoro.cryptoTracker.core.domain.util.NetworkError
import com.kenkoro.cryptoTracker.core.domain.util.Result
import com.kenkoro.cryptoTracker.core.domain.util.map
import com.kenkoro.cryptoTracker.crypto.data.mappers.toCoin
import com.kenkoro.cryptoTracker.crypto.data.networking.dto.CoinsResponseDto
import com.kenkoro.cryptoTracker.crypto.domain.Coin
import com.kenkoro.cryptoTracker.crypto.domain.CoinDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteCoinDataSource(
  private val httpClient: HttpClient,
) : CoinDataSource {
  override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
    return safeCall<CoinsResponseDto> {
      httpClient.get(
        urlString = constructUrl("/assets"),
      )
    }.map { response ->
      response.data.map { it.toCoin() }
    }
  }
}