package com.kenkoro.cryptoTracker.crypto.data.mappers

import com.kenkoro.cryptoTracker.crypto.data.networking.dto.CoinDto
import com.kenkoro.cryptoTracker.crypto.data.networking.dto.CoinPriceDto
import com.kenkoro.cryptoTracker.crypto.domain.Coin
import com.kenkoro.cryptoTracker.crypto.domain.CoinPrice
import java.time.Instant
import java.time.ZoneId

fun CoinDto.toCoin(): Coin {
  return Coin(
    id,
    rank,
    name,
    symbol,
    marketCapUsd,
    priceUsd,
    changePercent24Hr,
  )
}

fun CoinPriceDto.toCoinPrice(): CoinPrice {
  return CoinPrice(
    priceUsd = priceUsd,
    dateTime =
      Instant
        .ofEpochMilli(time)
        .atZone(ZoneId.systemDefault()),
  )
}