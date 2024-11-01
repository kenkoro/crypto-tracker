package com.kenkoro.cryptoTracker.crypto.presentation.coinList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kenkoro.cryptoTracker.core.domain.util.onError
import com.kenkoro.cryptoTracker.core.domain.util.onSuccess
import com.kenkoro.cryptoTracker.crypto.domain.Coin
import com.kenkoro.cryptoTracker.crypto.domain.CoinDataSource
import com.kenkoro.cryptoTracker.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
  private val dataSource: CoinDataSource,
) : ViewModel() {
  private val _state = MutableStateFlow(CoinListState())
  val state =
    _state
      .onStart { loadCoins() }
      .stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        CoinListState(),
      )
  private val _events = Channel<CoinListEvent>()
  val events = _events.receiveAsFlow()

  fun onAction(action: CoinListAction) {
    when (action) {
      is CoinListAction.OnCoinClick -> {
        _state.update { it.copy(selectedCoin = action.coinUi) }
      }
    }
  }

  private fun loadCoins() {
    viewModelScope.launch {
      _state.update { it.copy(isLoading = true) }
      dataSource
        .getCoins()
        .onSuccess { coins: List<Coin> ->
          _state.update { coinListState ->
            coinListState.copy(
              isLoading = false,
              coins = coins.map { it.toCoinUi() },
            )
          }
        }
        .onError { error ->
          _state.update { it.copy(isLoading = false) }
          _events.send(CoinListEvent.Error(error))
        }
    }
  }
}