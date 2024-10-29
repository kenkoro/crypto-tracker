package com.kenkoro.cryptoTracker.crypto.presentation.coinList

import androidx.compose.runtime.Immutable
import com.kenkoro.cryptoTracker.crypto.presentation.models.CoinUi

/*
List is not stable, so we need to make sure that this class won't change,
so there's no need to recompose out Ui frequently.
 */
@Immutable
data class CoinListState(
  val isLoading: Boolean,
  val coins: List<CoinUi> = emptyList(),
  val selectedCoin: CoinUi? = null,
)