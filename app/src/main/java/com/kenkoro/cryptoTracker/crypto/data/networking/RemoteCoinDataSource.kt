package com.kenkoro.cryptoTracker.crypto.data.networking

import com.kenkoro.cryptoTracker.core.data.networking.constructUrl
import com.kenkoro.cryptoTracker.core.data.networking.safeCall
import com.kenkoro.cryptoTracker.core.domain.util.NetworkError
import com.kenkoro.cryptoTracker.core.domain.util.Result
import com.kenkoro.cryptoTracker.core.domain.util.map
import com.kenkoro.cryptoTracker.crypto.data.mappers.toCoin
import com.kenkoro.cryptoTracker.crypto.data.mappers.toCoinPrice
import com.kenkoro.cryptoTracker.crypto.data.networking.dto.CoinsHistoryDto
import com.kenkoro.cryptoTracker.crypto.data.networking.dto.CoinsResponseDto
import com.kenkoro.cryptoTracker.crypto.domain.Coin
import com.kenkoro.cryptoTracker.crypto.domain.CoinDataSource
import com.kenkoro.cryptoTracker.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

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

  override suspend fun getCoinHistory(
    coinId: String,
    start: ZonedDateTime,
    end: ZonedDateTime,
  ): Result<List<CoinPrice>, NetworkError> {
    return safeCall<CoinsHistoryDto> {
      httpClient.get(
        urlString = constructUrl("/assets/$coinId/history"),
      ) {
        parameter("interval", "h6")
        parameter("start", start.toMillis())
        parameter("end", end.toMillis())
      }
    }.map { response ->
      response.data.map { it.toCoinPrice() }
    }
  }
}

private fun ZonedDateTime.toMillis(): Long {
  return this
    .withZoneSameInstant(ZoneId.of("UTC"))
    .toInstant()
    .toEpochMilli()
}