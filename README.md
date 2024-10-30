## Best practices from this project

### A good Ui is a dumb Ui
It's when you have a domain-defined ui models together with a Ui compatible models, so your Ui doesn't need to think of
formatting, or calculating the right data look to display. Example for the coin model:

#### Domain-defined model
```kotlin
data class Coin(
  val id: String,
  val rank: Int,
  val name: String,
  val symbol: String,
  val marketCapUsd: Double,
  val priceUsd: Double,
  val changePercent24Hr: Double,
)
```

#### Ui compatible model
```kotlin
data class CoinUi(
  val id: String,
  val rank: Int,
  val name: String,
  val symbol: String,
  val marketCapUsd: DisplayableNumber,
  val priceUsd: DisplayableNumber,
  val changePercent24Hr: DisplayableNumber,
  @DrawableRes val iconRes: Int,
)


data class DisplayableNumber(
  val value: Double,
  val formatted: String,
)


fun Coin.toCoinUi(): CoinUi {
  return CoinUi(
    id = id,
    rank = rank,
    name = name,
    symbol = symbol,
    marketCapUsd = marketCapUsd.toDisplayableNumber(),
    priceUsd = priceUsd.toDisplayableNumber(),
    changePercent24Hr = changePercent24Hr.toDisplayableNumber(),
    iconRes = getDrawableIdForCoin(symbol),
  )
}


fun Double.toDisplayableNumber(): DisplayableNumber {
  val formatter =
    NumberFormat.getNumberInstance(Locale.getDefault()).apply {
      minimumFractionDigits = 2
      maximumFractionDigits = 2
    }


  return DisplayableNumber(
    value = this,
    formatted = formatter.format(this),
  )
}
```

### New Result class
Result utility class with a concrete set of errors, and error massages.

#### P.s.: `Error` is a plain marker interface just for you
```kotlin
typealias RootError = Error

sealed interface Result<out D, out E : Error> {
  data class Success<out D>(val data: D) : Result<D, Nothing>

  data class Error<out E : RootError>(val error: E) : Result<Nothing, E>
}
```

Here, we use `out` to mark `D`, and `E` as covarient type parameters - we can omit them later by using
`Nothing`, since `Nothing` is a supertype of all types in Kotlin. E.g. our `out E` can accept all 
implementations of `Error`, and also **out of** `E`. This allows us to write something like:

```kotlin
suspend inline fun <reified T> networkCall(response: HttpResponse): Result<T, NetworkError> {
  return when (response.status.value) {
    in 200..299 -> {
      try {
        Result.Success(response.body<T>())
      } catch (e: NoTransformationFoundException) {
        Result.Error(NetworkError.Serialization)
      }
    }
    // TODO
  }
}
```

#### P.s.: Want to map your enum error value to a specific string resource? [Check this out][string-resource-wo-context]

### Dto as a way of independence for your domain
You should use dto classes instead of your domain models, so that you don't rely on a specific Api implementation. I mean, your
app shouldn't be heavily dependent on other codebases, because if the 3rd party code changes, it can affect your app *well-being*.


[string-resource-wo-context]: https://youtu.be/mB1Lej0aDus?si=yzYd-7Ndp3Jjy-Ie
