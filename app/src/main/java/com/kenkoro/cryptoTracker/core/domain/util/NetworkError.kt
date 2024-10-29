package com.kenkoro.cryptoTracker.core.domain.util

enum class NetworkError : Error {
  RequestTimeout,
  TooManyRequests,
  NoInternet,
  ServerError,
  Serialization,
  Unknown,
}