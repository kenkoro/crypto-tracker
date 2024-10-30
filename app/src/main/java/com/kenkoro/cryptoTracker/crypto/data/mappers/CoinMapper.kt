package com.kenkoro.cryptoTracker.crypto.data.mappers

import com.kenkoro.cryptoTracker.crypto.data.networking.dto.CoinDto
import com.kenkoro.cryptoTracker.crypto.domain.Coin

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