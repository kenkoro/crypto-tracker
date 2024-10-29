package com.kenkoro.cryptoTracker.crypto.presentation.coinList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.kenkoro.cryptoTracker.core.presentation.local.LocalArrangement
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.composables.CoinListItem
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.composables.previewCoin
import com.kenkoro.cryptoTracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListScreen(
  modifier: Modifier = Modifier,
  state: CoinListState,
) {
  val arrangement = LocalArrangement.current

  if (state.isLoading) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator()
    }
  } else {
    LazyColumn(
      modifier = modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(arrangement.coinListScreenLazyColumn),
    ) {
      items(state.coins) { coinUi ->
        CoinListItem(
          coinUi = coinUi,
          onClick = {},
          modifier = Modifier.fillMaxWidth(),
        )
        HorizontalDivider()
      }
    }
  }
}

@PreviewLightDark
@Composable
private fun CoinListScreenPrev() {
  CryptoTrackerTheme {
    CoinListScreen(
      state = previewCoinListState,
      modifier = Modifier.background(MaterialTheme.colorScheme.background),
    )
  }
}

internal val previewCoinListState =
  CoinListState(
    isLoading = false,
    coins = (1..10).map { previewCoin.copy(id = it.toString()) },
  )