package com.kenkoro.cryptoTracker.core.presentation.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Padding(
  val coinListItem: Dp = 16.dp,
  val priceChange: Dp = 4.dp,
  val coinDetailScreenColumn: Dp = coinListItem,
  val infoCard: Dp = 8.dp,
  val infoCardIconTop: Dp = coinListItem,
  val infoCardText: Dp = coinListItem,
  val infoCardTitle: Dp = infoCardText,
)

val LocalPadding = compositionLocalOf { Padding() }