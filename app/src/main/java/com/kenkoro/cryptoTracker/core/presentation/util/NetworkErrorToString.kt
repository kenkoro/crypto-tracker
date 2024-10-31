package com.kenkoro.cryptoTracker.core.presentation.util

import android.content.Context
import com.kenkoro.cryptoTracker.R
import com.kenkoro.cryptoTracker.core.domain.util.NetworkError

fun NetworkError.toString(context: Context): String {
  val redId =
    when (this) {
      NetworkError.RequestTimeout -> R.string.error_request_timeout
      NetworkError.TooManyRequests -> R.string.error_too_many_requests
      NetworkError.NoInternet -> R.string.error_no_internet
      NetworkError.ServerError -> R.string.error_unknown
      NetworkError.Serialization -> R.string.error_serialization
      NetworkError.Unknown -> R.string.error_unknown
    }
  return context.getString(redId)
}