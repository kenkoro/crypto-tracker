package com.kenkoro.cryptoTracker.core.data.networking

import com.kenkoro.cryptoTracker.BuildConfig

fun constructUrl(url: String): String {
  return when {
    url.startsWith(BuildConfig.BASE_URL) -> url
    url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)
    else -> url
  }
}