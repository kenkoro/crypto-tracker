package com.kenkoro.cryptoTracker.core.presentation.local

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class FontSize(
  val coinListItemSymbolText: TextUnit = 20.sp,
  val coinListItemNameText: TextUnit = 14.sp,
  val coinListItemPriceUsdText: TextUnit = 16.sp,
  val priceChangeText: TextUnit = coinListItemNameText,
)

val LocalFontSize = compositionLocalOf { FontSize() }