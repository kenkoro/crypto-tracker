package com.kenkoro.cryptoTracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenkoro.cryptoTracker.core.presentation.util.ObserveAsEvents
import com.kenkoro.cryptoTracker.core.presentation.util.toString
import com.kenkoro.cryptoTracker.crypto.presentation.coinDetail.CoinDetailScreen
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListEvent
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListScreen
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListViewModel
import com.kenkoro.cryptoTracker.ui.theme.CryptoTrackerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CryptoTrackerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          val viewModel = koinViewModel<CoinListViewModel>()
          val state by viewModel.state.collectAsStateWithLifecycle()
          ObserveAsEvents(events = viewModel.events) { event ->
            when (event) {
              is CoinListEvent.Error -> {
                Toast.makeText(
                  this,
                  event.error.toString(this),
                  Toast.LENGTH_LONG,
                ).show()
              }
            }
          }
          when {
            state.selectedCoin != null -> {
              CoinDetailScreen(state = state, modifier = Modifier.padding(innerPadding))
            }

            else -> {
              CoinListScreen(
                state = state,
                onAction = viewModel::onAction,
                modifier = Modifier.padding(innerPadding),
              )
            }
          }
        }
      }
    }
  }
}