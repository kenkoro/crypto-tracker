package com.kenkoro.cryptoTracker.crypto.presentation.coinDetail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kenkoro.cryptoTracker.R
import com.kenkoro.cryptoTracker.core.presentation.local.LocalFontSize
import com.kenkoro.cryptoTracker.core.presentation.local.LocalPadding
import com.kenkoro.cryptoTracker.core.presentation.local.LocalSize
import com.kenkoro.cryptoTracker.ui.theme.CryptoTrackerTheme

@Composable
fun InfoCard(
  modifier: Modifier = Modifier,
  title: String,
  formattedText: String,
  icon: ImageVector,
  contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
  val padding = LocalPadding.current
  val size = LocalSize.current
  val fontSize = LocalFontSize.current
  val defaultTextStyle =
    LocalTextStyle.current.copy(
      textAlign = TextAlign.Center,
      fontSize = 18.sp,
      color = contentColor,
    )

  Card(
    modifier =
      modifier
        .padding(padding.infoCard)
        .shadow(
          elevation = 15.dp,
          ambientColor = MaterialTheme.colorScheme.primary,
          spotColor = MaterialTheme.colorScheme.primary,
          shape = RectangleShape,
        ),
    shape = RectangleShape,
    border =
      BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.primary,
      ),
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = contentColor,
      ),
  ) {
    AnimatedContent(
      targetState = icon,
      modifier = Modifier.align(Alignment.CenterHorizontally),
      label = "IconAnimation",
    ) { icon ->
      Icon(
        imageVector = icon,
        contentDescription = title,
        modifier =
          Modifier
            .size(size.infoCardIcon)
            .padding(top = padding.infoCardIconTop),
        tint = contentColor,
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    AnimatedContent(
      targetState = formattedText,
      modifier = Modifier.align(Alignment.CenterHorizontally),
      label = "ValueAnimation",
    ) { formattedText ->
      Text(
        text = formattedText,
        style = defaultTextStyle,
        modifier =
          Modifier
            .padding(horizontal = padding.infoCardText),
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = title,
      textAlign = TextAlign.Center,
      modifier =
        Modifier
          .padding(horizontal = padding.infoCardTitle)
          .padding(bottom = padding.infoCardTitle)
          .align(Alignment.CenterHorizontally),
      fontSize = fontSize.infoCardTitle,
      fontWeight = FontWeight.Light,
      color = contentColor,
    )
  }
}

@PreviewLightDark
@Composable
private fun InfoCardPrev() {
  CryptoTrackerTheme {
    InfoCard(
      title = "Price",
      formattedText = "$ 64,345,234,234.4",
      icon = ImageVector.vectorResource(R.drawable.btc),
    )
  }
}