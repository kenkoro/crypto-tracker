package com.kenkoro.cryptoTracker.core.locals

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class FontSize(
  val coinListItemSymbolText: TextUnit = 20.sp,
)

val LocalFontSize = compositionLocalOf { FontSize() }