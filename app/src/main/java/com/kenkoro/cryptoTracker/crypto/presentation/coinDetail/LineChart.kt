package com.kenkoro.cryptoTracker.crypto.presentation.coinDetail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kenkoro.cryptoTracker.crypto.domain.CoinPrice
import com.kenkoro.cryptoTracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
  modifier: Modifier = Modifier,
  dataPoints: List<DataPoint>,
  style: ChartStyle,
  visibleDataPointsIndices: IntRange,
  unit: String,
  selectedDataPoint: DataPoint? = null,
  onSelectedDataPoint: (DataPoint) -> Unit = {},
  onXLabelWidthChange: (Float) -> Unit = {},
  showHelperLines: Boolean = true,
) {
  val textStyle =
    LocalTextStyle.current.copy(
      fontSize = style.labelFontSize,
    )
  val visibleDataPoints =
    remember(dataPoints, visibleDataPointsIndices) {
      dataPoints.slice(visibleDataPointsIndices)
    }
  val maxYValue =
    remember(visibleDataPoints) {
      visibleDataPoints.maxOfOrNull { it.y } ?: 0F
    }
  val minYValue =
    remember(visibleDataPoints) {
      visibleDataPoints.minOfOrNull { it.y } ?: 0F
    }
  val measurer = rememberTextMeasurer()
  var xLabelWidth by remember {
    mutableFloatStateOf(0F)
  }
  LaunchedEffect(xLabelWidth) {
    onXLabelWidthChange(xLabelWidth)
  }
  val selectedDataPointIndex =
    remember(selectedDataPoint) {
      dataPoints.indexOf(selectedDataPoint)
    }
  var drawPoints by remember {
    mutableStateOf(listOf<DataPoint>())
  }
  var isShowingDataPoints by remember {
    mutableStateOf(selectedDataPoint != null)
  }

  Canvas(
    modifier =
      modifier
        .fillMaxSize()
        .pointerInput(drawPoints, xLabelWidth) {
          detectHorizontalDragGestures { change, _ ->
            val newSelectedDataPointIndex =
              getSelectedDataPointIndex(
                touchOffsetX = change.position.x,
                triggerWidth = xLabelWidth,
                drawPoints = drawPoints,
              )
            isShowingDataPoints =
              (newSelectedDataPointIndex + visibleDataPointsIndices.first) in
              visibleDataPointsIndices
            if (isShowingDataPoints) {
              onSelectedDataPoint(dataPoints[newSelectedDataPointIndex])
            }
          }
        },
  ) {
    val minYLabelSpacingPx = style.minYLabelSpacing.roundToPx()
    val verticalPaddingPx = style.verticalPadding.roundToPx()
    val horizontalPaddingPx = style.horizontalPadding.toPx()
    val xAxisLabelSpacingPx = style.xAxisLabelSpacing.toPx()

    val xLabelTextLayoutResults =
      visibleDataPoints.map {
        measurer.measure(
          text = it.xLabel,
          style = textStyle.copy(textAlign = TextAlign.Center),
        )
      }
    val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
    val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0
    val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?: 0
    val xLabelLineHeight =
      if (maxXLabelLineCount > 0) {
        maxXLabelHeight / maxXLabelLineCount
      } else {
        0
      }

    val viewPortHeightPx =
      size.height -
        (maxXLabelHeight + 2 * verticalPaddingPx + xLabelLineHeight + xAxisLabelSpacingPx)

    val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
    val labelCountExcludingLastLabel =
      (labelViewPortHeightPx / (xLabelLineHeight + minYLabelSpacingPx)).toInt()

    val valueIncrement = (maxYValue - minYValue) / labelCountExcludingLastLabel
    val yLabels =
      (0..labelCountExcludingLastLabel).map {
        ValueLabel(
          value = maxYValue - (valueIncrement * it),
          unit = unit,
        )
      }
    val yLabelTextLayoutResults =
      yLabels.map {
        measurer.measure(
          text = it.formatted(),
          style = textStyle,
        )
      }
    val maxYLabelWidth = yLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0

    val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10F
    val viewPortRightX = size.width
    val viewPortBottomY = viewPortTopY + viewPortHeightPx
    val viewPortLeftX = 2F * horizontalPaddingPx + maxYLabelWidth
    val viewPort =
      Rect(
        left = viewPortLeftX,
        top = viewPortTopY,
        right = viewPortRightX,
        bottom = viewPortBottomY,
      )

    xLabelWidth = maxXLabelWidth + xAxisLabelSpacingPx
    xLabelTextLayoutResults.forEachIndexed { index, result ->
      val x = viewPortLeftX + xAxisLabelSpacingPx / 2F + xLabelWidth * index
      drawText(
        textLayoutResult = result,
        topLeft =
          Offset(
            x = x,
            y = viewPortBottomY + xAxisLabelSpacingPx,
          ),
        color =
          if (selectedDataPointIndex == index) {
            style.selectedColor
          } else {
            style.unselectedColor
          },
      )

      if (showHelperLines) {
        val xHelperLine = x + result.size.width / 2F
        drawLine(
          color =
            if (selectedDataPointIndex == index) {
              style.selectedColor
            } else {
              style.unselectedColor
            },
          start =
            Offset(
              x = xHelperLine,
              y = viewPortBottomY,
            ),
          end =
            Offset(
              x = xHelperLine,
              y = viewPortTopY,
            ),
          strokeWidth =
            if (selectedDataPointIndex == index) {
              style.helperLinesThicknessPx * 2F
            } else {
              style.helperLinesThicknessPx
            },
        )
      }

      if (selectedDataPointIndex == index) {
        val valueLabel =
          ValueLabel(
            value = visibleDataPoints[index].y,
            unit = unit,
          )
        val valueResult =
          measurer.measure(
            text = valueLabel.formatted(),
            style = textStyle.copy(color = style.selectedColor),
            maxLines = 1,
          )
        val textPositionX =
          if (selectedDataPointIndex == visibleDataPointsIndices.last) {
            x - valueResult.size.width
          } else {
            x - valueResult.size.width / 2F
          } + result.size.width / 2F
        val isTextInVisibleRange =
          (size.width - textPositionX).roundToInt() in 0..size.width.roundToInt()

        if (isTextInVisibleRange) {
          drawText(
            textLayoutResult = valueResult,
            topLeft =
              Offset(
                x = textPositionX,
                y = viewPortTopY - valueResult.size.height - 10F,
              ),
          )
        }
      }
    }

    val heightRequiredForLabels = xLabelLineHeight * (labelCountExcludingLastLabel + 1)
    val remainingHeightForLabels = labelViewPortHeightPx - heightRequiredForLabels
    val spaceBetweenLabels = remainingHeightForLabels / labelCountExcludingLastLabel
    yLabelTextLayoutResults.forEachIndexed { index, result ->
      val x = horizontalPaddingPx + maxYLabelWidth - result.size.width.toFloat()
      val y = viewPortTopY + index * (xLabelLineHeight + spaceBetweenLabels) - xLabelLineHeight / 2F
      drawText(
        textLayoutResult = result,
        topLeft =
          Offset(
            x = x,
            y = y,
          ),
        color = style.unselectedColor,
      )

      if (showHelperLines) {
        val yHelperLine = y + result.size.height / 2F
        drawLine(
          color = style.unselectedColor,
          start =
            Offset(
              x = viewPortLeftX,
              y = yHelperLine,
            ),
          end =
            Offset(
              x = viewPortRightX,
              y = yHelperLine,
            ),
          strokeWidth = style.helperLinesThicknessPx,
        )
      }
    }

    drawPoints =
      visibleDataPointsIndices.map { index ->
        val x =
          viewPortLeftX + (index - visibleDataPointsIndices.first) * xLabelWidth + xLabelWidth / 2F
        val ratio = (dataPoints[index].y - minYValue) / (maxYValue - minYValue)
        val y = viewPortBottomY - (ratio * viewPortHeightPx)
        DataPoint(
          x = x,
          y = y,
          xLabel = dataPoints[index].xLabel,
        )
      }

    val conPoints1 = mutableListOf<DataPoint>()
    val conPoints2 = mutableListOf<DataPoint>()
    for (i in 1 until drawPoints.size) {
      val p0 = drawPoints[i - 1]
      val p1 = drawPoints[i]

      val x = (p1.x + p0.x) / 2F
      val y1 = p0.y
      val y2 = p1.y

      conPoints1 += DataPoint(x, y1, "")
      conPoints2 += DataPoint(x, y2, "")
    }

    val linePath =
      Path().apply {
        if (drawPoints.isNotEmpty()) {
          moveTo(drawPoints.first().x, drawPoints.first().y)
          for (i in 1 until drawPoints.size) {
            cubicTo(
              x1 = conPoints1[i - 1].x,
              y1 = conPoints1[i - 1].y,
              x2 = conPoints2[i - 1].x,
              y2 = conPoints2[i - 1].y,
              x3 = drawPoints[i].x,
              y3 = drawPoints[i].y,
            )
          }
        }
      }
    drawPath(
      path = linePath,
      color = style.chartLineColor,
      style =
        Stroke(
          width = 5F,
          cap = StrokeCap.Round,
        ),
    )

    drawPoints.forEachIndexed { index, dataPoint ->
      if (isShowingDataPoints) {
        val circleOffset =
          Offset(
            x = dataPoint.x,
            y = dataPoint.y,
          )

        if (selectedDataPointIndex == index) {
          drawCircle(
            color = Color.White,
            radius = 15F,
            center = circleOffset,
          )
          drawCircle(
            color = style.selectedColor,
            radius = 15F,
            center = circleOffset,
            style = Stroke(width = 3F),
          )
        } else {
          drawCircle(
            color = style.selectedColor,
            radius = 10F,
            center = circleOffset,
          )
        }
      }
    }
  }
}

private fun getSelectedDataPointIndex(
  touchOffsetX: Float,
  triggerWidth: Float,
  drawPoints: List<DataPoint>,
): Int {
  val triggerRangeLeft = touchOffsetX - triggerWidth / 2F
  val triggerRangeRight = touchOffsetX + triggerWidth / 2F
  return drawPoints.indexOfFirst {
    it.x in triggerRangeLeft..triggerRangeRight
  }
}

@Preview
@Composable
private fun LineChartPrev() {
  CryptoTrackerTheme {
    val coinHistoryRandomized =
      remember {
        (1..30).map {
          CoinPrice(
            priceUsd = Random.nextDouble(),
            dateTime = ZonedDateTime.now().plusHours(it.toLong()),
          )
        }
      }
    val chartStyle =
      ChartStyle(
        chartLineColor = Color.Black,
        unselectedColor = Color(0xFF7C7C7C),
        selectedColor = Color.Black,
        axisLinesThicknessPx = 5F,
        helperLinesThicknessPx = 1F,
        labelFontSize = 14.sp,
        minYLabelSpacing = 25.dp,
        verticalPadding = 8.dp,
        horizontalPadding = 8.dp,
        xAxisLabelSpacing = 8.dp,
      )
    val dataPoints =
      remember {
        coinHistoryRandomized.map {
          DataPoint(
            x = it.dateTime.hour.toFloat(),
            y = it.priceUsd.toFloat(),
            xLabel =
              DateTimeFormatter
                .ofPattern("ha\nM/d")
                .format(it.dateTime),
          )
        }
      }

    LineChart(
      dataPoints = dataPoints,
      style = chartStyle,
      visibleDataPointsIndices = 0..10,
      unit = "$",
      modifier =
        Modifier
          .size(width = 700.dp, height = 300.dp)
          .background(Color.White),
      selectedDataPoint = dataPoints[3],
    )
  }
}