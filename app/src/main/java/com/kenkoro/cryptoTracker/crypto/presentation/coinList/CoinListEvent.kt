package com.kenkoro.cryptoTracker.crypto.presentation.coinList

import com.kenkoro.cryptoTracker.core.domain.util.NetworkError

sealed interface CoinListEvent {
  data class Error(val error: NetworkError) : CoinListEvent
}