package com.kenkoro.cryptoTracker.core.locals

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Padding(
  val coinListItem: Dp = 16.dp,
)

val LocalPadding = compositionLocalOf { Padding() }