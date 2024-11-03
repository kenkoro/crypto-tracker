package com.kenkoro.cryptoTracker.crypto.presentation.coinDetail

import java.text.NumberFormat
import java.util.Locale

data class ValueLabel(
  val value: Float,
  val unit: String,
) {
  fun formatted(): String {
    val formatter =
      NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        val fractionDigits =
          when {
            value > 1000 -> 0
            value in 2F..999F -> 2
            else -> 3
          }
        maximumFractionDigits = fractionDigits
        minimumFractionDigits = 0
      }
    return "${formatter.format(value)}$unit"
  }
}