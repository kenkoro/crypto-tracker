package com.kenkoro.cryptoTracker.core.presentation.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
  val coinListItemIcon: Dp = 85.dp,
  val priceChangeIcon: Dp = 20.dp,
  val coinDetailScreenIcon: Dp = 100.dp,
  val infoCardIcon: Dp = 75.dp,
)

val LocalSize = compositionLocalOf { Size() }