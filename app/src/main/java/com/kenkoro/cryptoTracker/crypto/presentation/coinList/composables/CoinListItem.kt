package com.kenkoro.cryptoTracker.crypto.presentation.coinList.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kenkoro.cryptoTracker.core.presentation.local.LocalArrangement
import com.kenkoro.cryptoTracker.core.presentation.local.LocalFontSize
import com.kenkoro.cryptoTracker.core.presentation.local.LocalPadding
import com.kenkoro.cryptoTracker.core.presentation.local.LocalSize
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

  val contentColor =
    if (isSystemInDarkTheme()) {
      Color.White
    } else {
      Color.Black
    }

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
        fontWeight = FontWeight.Bold,
        color = contentColor,
      )
      Text(
        text = coinUi.name,
        fontSize = fontSize.coinListItemNameText,
        fontWeight = FontWeight.Light,
        color = contentColor,
      )
    }
    Column(
      horizontalAlignment = Alignment.End,
    ) {
      Text(
        text = "$ ${coinUi.priceUsd.formatted}",
        fontSize = fontSize.coinListItemPriceUsdText,
        fontWeight = FontWeight.Bold,
        color = contentColor,
      )
      Spacer(modifier = Modifier.height(8.dp))
      PriceChange(change = coinUi.changePercent24Hr)
    }
  }
}

@PreviewLightDark
@Composable
private fun CoinListItemPrev() {
  CryptoTrackerTheme {
    CoinListItem(
      coinUi = previewCoin,
      onClick = {},
      modifier = Modifier.background(MaterialTheme.colorScheme.background),
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
    priceUsd = 62234.2345233,
    changePercent24Hr = -0.1,
  ).toCoinUi()