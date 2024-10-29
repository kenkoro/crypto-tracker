package com.kenkoro.cryptoTracker.crypto.domain

import com.kenkoro.cryptoTracker.core.domain.util.NetworkError
import com.kenkoro.cryptoTracker.core.domain.util.Result

interface CoinDataSource {
  suspend fun getCoins(): Result<List<Coin>, NetworkError>
}