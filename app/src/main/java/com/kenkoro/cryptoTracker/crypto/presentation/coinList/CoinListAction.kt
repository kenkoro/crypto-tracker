package com.kenkoro.cryptoTracker.crypto.presentation.coinList

import com.kenkoro.cryptoTracker.crypto.presentation.models.CoinUi

sealed interface CoinListAction {
  data class OnCoinClick(val coinUi: CoinUi) : CoinListAction
}