package com.kenkoro.cryptoTracker.core.presentation.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Arrangement(
  val coinListItem: Dp = 16.dp,
  val coinListScreenLazyColumn: Dp = 8.dp,
)

val LocalArrangement = compositionLocalOf { Arrangement() }