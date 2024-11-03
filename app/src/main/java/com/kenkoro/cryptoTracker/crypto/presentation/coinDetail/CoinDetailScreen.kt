@file:OptIn(ExperimentalLayoutApi::class)

package com.kenkoro.cryptoTracker.crypto.presentation.coinDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.kenkoro.cryptoTracker.R
import com.kenkoro.cryptoTracker.core.presentation.local.LocalFontSize
import com.kenkoro.cryptoTracker.core.presentation.local.LocalPadding
import com.kenkoro.cryptoTracker.core.presentation.local.LocalSize
import com.kenkoro.cryptoTracker.crypto.presentation.coinDetail.components.InfoCard
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListState
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.components.previewCoin
import com.kenkoro.cryptoTracker.crypto.presentation.models.CoinUi
import com.kenkoro.cryptoTracker.crypto.presentation.models.toDisplayableNumber
import com.kenkoro.cryptoTracker.ui.theme.CryptoTrackerTheme
import com.kenkoro.cryptoTracker.ui.theme.greenBackground

@Composable
fun CoinDetailScreen(
  modifier: Modifier = Modifier,
  state: CoinListState,
) {
  val padding = LocalPadding.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current
  val contentColor =
    if (isSystemInDarkTheme()) {
      Color.White
    } else {
      Color.Black
    }

  if (state.isLoading) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      CircularProgressIndicator()
    }
  } else if (state.selectedCoin != null) {
    val coin: CoinUi = state.selectedCoin

    Column(
      modifier =
        modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(padding.coinDetailScreenColumn),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Icon(
        imageVector = ImageVector.vectorResource(coin.iconRes),
        contentDescription = coin.name,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(size.coinDetailScreenIcon),
      )
      Text(
        text = coin.name,
        color = contentColor,
        fontSize = fontSize.coinDetailScreenName,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
      )
      Text(
        text = coin.symbol,
        color = contentColor,
        fontSize = fontSize.coinDetailScreenSymbol,
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Center,
      )
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
      ) {
        InfoCard(
          title = stringResource(id = R.string.market_cap),
          formattedText = "$ ${coin.marketCapUsd.formatted}",
          icon = ImageVector.vectorResource(id = R.drawable.stock),
        )
        InfoCard(
          title = stringResource(id = R.string.price),
          formattedText = "$ ${coin.priceUsd.formatted}",
          icon = ImageVector.vectorResource(id = R.drawable.dollar),
        )

        val absoluteChangeFormatted =
          (coin.priceUsd.value * (coin.changePercent24Hr.value / 100))
            .toDisplayableNumber()
        val isPositive = coin.changePercent24Hr.value > 0.0
        val changeLast24HrContentColor =
          if (isPositive) {
            if (isSystemInDarkTheme()) Color.Green else greenBackground
          } else {
            MaterialTheme.colorScheme.error
          }
        InfoCard(
          title = stringResource(id = R.string.change_last_24hr),
          formattedText = "$ ${absoluteChangeFormatted.formatted}",
          icon =
            if (isPositive) {
              ImageVector.vectorResource(R.drawable.trending)
            } else {
              ImageVector.vectorResource(id = R.drawable.trending_down)
            },
          contentColor = changeLast24HrContentColor,
        )
      }
      AnimatedVisibility(visible = coin.coinPriceHistory.isNotEmpty()) {
        var selectedDataPoint by remember {
          mutableStateOf<DataPoint?>(null)
        }
        var labelWidth by remember {
          mutableFloatStateOf(0F)
        }
        var totalChartWidth by remember {
          mutableFloatStateOf(0F)
        }
        val amountOfVisibleDataPoints =
          if (labelWidth > 0) {
            ((totalChartWidth - 2.5 * labelWidth) / labelWidth).toInt()
          } else {
            0
          }
        val startIndex =
          (coin.coinPriceHistory.lastIndex - amountOfVisibleDataPoints)
            .coerceAtLeast(0)
        LineChart(
          dataPoints = coin.coinPriceHistory,
          style =
            ChartStyle(
              chartLineColor = MaterialTheme.colorScheme.primary,
              unselectedColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2F),
              selectedColor = MaterialTheme.colorScheme.primary,
              helperLinesThicknessPx = 5F,
              axisLinesThicknessPx = 5F,
              labelFontSize = fontSize.lineChartDateTimeLabel,
              minYLabelSpacing = 25.dp,
              verticalPadding = 8.dp,
              horizontalPadding = 8.dp,
              xAxisLabelSpacing = 8.dp,
            ),
          visibleDataPointsIndices = startIndex..coin.coinPriceHistory.lastIndex,
          unit = "$",
          modifier =
            Modifier
              .fillMaxWidth()
              .aspectRatio(16 / 9F)
              .onSizeChanged { totalChartWidth = it.width.toFloat() },
          selectedDataPoint = selectedDataPoint,
          onSelectedDataPoint = { selectedDataPoint = it },
          onXLabelWidthChange = { labelWidth = it },
        )
      }
    }
  } else {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text(
        text = stringResource(id = R.string.no_coin_is_selected),
        color = MaterialTheme.colorScheme.primary,
        fontSize = fontSize.noCoinIsSelected,
      )
    }
  }
}

@PreviewLightDark
@Composable
private fun CoinDetailScreenPrev() {
  CryptoTrackerTheme {
    CoinDetailScreen(
      state = CoinListState(selectedCoin = previewCoin),
      modifier = Modifier.background(MaterialTheme.colorScheme.background),
    )
  }
}