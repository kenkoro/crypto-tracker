package com.kenkoro.cryptoTracker.crypto.presentation.coinList.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.cryptoTracker.core.locals.LocalArrangement
import com.kenkoro.cryptoTracker.core.locals.LocalFontSize
import com.kenkoro.cryptoTracker.core.locals.LocalPadding
import com.kenkoro.cryptoTracker.core.locals.LocalSize
import com.kenkoro.cryptoTracker.crypto.domain.Coin
import com.kenkoro.cryptoTracker.crypto.presentation.models.CoinUi
import com.kenkoro.cryptoTracker.crypto.presentation.models.toCoinUi
import com.kenkoro.cryptoTracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListItem(
  modifier: Modifier = Modifier,
  coinUi: CoinUi,
  onClick: () -> Unit,
) {
  val padding = LocalPadding.current
  val arrangement = LocalArrangement.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current

  Row(
    modifier =
      modifier
        .clickable(onClick = onClick)
        .padding(padding.coinListItem),
    horizontalArrangement = Arrangement.spacedBy(arrangement.coinListItem),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(coinUi.iconRes),
      contentDescription = coinUi.name,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(size.coinListItemIcon),
    )
    Column(
      modifier = Modifier.weight(1F),
    ) {
      Text(
        text = coinUi.symbol,
        fontSize = fontSize.coinListItemSymbolText,
      )
    }
  }
}

@Preview
@Composable
private fun CoinListItemPrev() {
  CryptoTrackerTheme {
    CoinListItem(
      coinUi = previewCoin,
      onClick = {},
      modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
    )
  }
}

internal val previewCoin =
  Coin(
    id = "bitcoin",
    rank = 1,
    name = "Bitcoin",
    symbol = "BTC",
    marketCapUsd = 1231242132.75,
    priceUsd = 62.2345233,
    changePercent24Hr = 0.1,
  ).toCoinUi()