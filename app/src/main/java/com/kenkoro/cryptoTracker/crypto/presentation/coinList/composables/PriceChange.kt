package com.kenkoro.cryptoTracker.crypto.presentation.coinList.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.kenkoro.cryptoTracker.core.presentation.local.LocalFontSize
import com.kenkoro.cryptoTracker.core.presentation.local.LocalPadding
import com.kenkoro.cryptoTracker.core.presentation.local.LocalSize
import com.kenkoro.cryptoTracker.crypto.presentation.models.DisplayableNumber
import com.kenkoro.cryptoTracker.ui.theme.CryptoTrackerTheme
import com.kenkoro.cryptoTracker.ui.theme.greenBackground

@Composable
fun PriceChange(
  modifier: Modifier = Modifier,
  change: DisplayableNumber,
) {
  val padding = LocalPadding.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current

  val (contentColor, backgroundColor, icon) =
    if (change.value < 0.0) {
      Triple(
        MaterialTheme.colorScheme.onErrorContainer,
        MaterialTheme.colorScheme.errorContainer,
        Icons.Default.KeyboardArrowDown,
      )
    } else {
      Triple(
        Color.Green,
        greenBackground,
        Icons.Default.KeyboardArrowUp,
      )
    }

  Row(
    modifier =
      modifier
        .clip(RoundedCornerShape(100F))
        .background(backgroundColor)
        .padding(padding.priceChange),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = "PriceChangeIcon",
      modifier = Modifier.size(size.priceChangeIcon),
      tint = contentColor,
    )
    Text(
      text = "${change.formatted} %",
      fontSize = fontSize.priceChangeText,
      color = contentColor,
      fontWeight = FontWeight.Medium,
    )
  }
}

@Preview
@Composable
private fun PriceChangePrev() {
  CryptoTrackerTheme {
    PriceChange(change = previewCoin.changePercent24Hr)
  }
}