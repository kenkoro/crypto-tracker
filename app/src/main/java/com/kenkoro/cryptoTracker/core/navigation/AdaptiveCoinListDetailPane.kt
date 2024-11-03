@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.kenkoro.cryptoTracker.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenkoro.cryptoTracker.core.presentation.util.ObserveAsEvents
import com.kenkoro.cryptoTracker.core.presentation.util.toString
import com.kenkoro.cryptoTracker.crypto.presentation.coinDetail.CoinDetailScreen
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListAction
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListEvent
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListScreen
import com.kenkoro.cryptoTracker.crypto.presentation.coinList.CoinListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdaptiveCoinListDetailPane(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val viewModel = koinViewModel<CoinListViewModel>()
  val state by viewModel.state.collectAsStateWithLifecycle()
  ObserveAsEvents(events = viewModel.events) { event ->
    when (event) {
      is CoinListEvent.Error -> {
        Toast.makeText(
          context,
          event.error.toString(context),
          Toast.LENGTH_LONG,
        ).show()
      }
    }
  }

  val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
  NavigableListDetailPaneScaffold(
    navigator = navigator,
    listPane = {
      AnimatedPane {
        CoinListScreen(
          state = state,
          onAction = { action ->
            viewModel.onAction(action)
            when (action) {
              is CoinListAction.OnCoinClick -> {
                navigator.navigateTo(
                  pane = ListDetailPaneScaffoldRole.Detail,
                )
              }
            }
          },
        )
      }
    },
    detailPane = {
      AnimatedPane {
        CoinDetailScreen(state = state)
      }
    },
    modifier = modifier,
  )
}